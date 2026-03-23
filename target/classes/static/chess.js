/* ================= GLOBAL STATE ================= */

let gameId = null;
let selected = null;
let boardState = [];

/* ================= AUTH ================= */

function selectRole(role) {
    document.getElementById("roleScreen").classList.add("hidden");
    document.getElementById("loginScreen").classList.remove("hidden");
}

function register() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/api/users/register", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ username, password })
    })
    .then(() => alert("User registered!"));
}

function login() {
    const username = document.getElementById("username").value;

    fetch("/api/users")
        .then(res => res.json())
        .then(users => {
            const user = users.find(u => u.username === username);

            if (!user) {
                alert("User not found! Register first.");
                return;
            }

            document.getElementById("loginScreen").classList.add("hidden");
            document.getElementById("gameScreen").classList.remove("hidden");

            createGame();
        });
}

/* ================= GAME INIT ================= */

function createGame() {
    fetch("/api/games", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({})
    })
    .then(res => {
        if (!res.ok) throw new Error("Game creation failed");
        return res.json();
    })
    .then(game => {
        gameId = game.id;
        console.log("Game created:", gameId);
        initGame();
    })
    .catch(err => {
        console.error(err);
        alert("Failed to create game");
    });
}

function initGame() {
    if (!gameId) return;

    loadBoard();
    loadHistory();
    loadTurn();
}

/* ================= BOARD ================= */

function loadBoard() {
    fetch(`/api/games/${gameId}/board`)
        .then(res => res.json())
        .then(board => {
            boardState = board;
            drawBoard(board);
        });
}

function drawBoard(board) {
    const boardDiv = document.getElementById("board");
    boardDiv.innerHTML = "";

    for (let r = 0; r < 8; r++) {
        for (let c = 0; c < 8; c++) {

            const sq = document.createElement("div");
            sq.className = "square " + ((r + c) % 2 === 0 ? "light" : "dark");
            sq.onclick = () => clickSquare(r, c);

            const piece = board[r][c];

            if (piece) {
                const img = document.createElement("img");
                img.src = "/pieces/" + piece + ".svg";
                img.className = "piece";
                sq.appendChild(img);
            }

            boardDiv.appendChild(sq);
        }
    }
}

/* ================= CLICK ================= */

function clickSquare(r, c) {

    const piece = boardState[r][c];

    if (!selected) {
        if (!piece) return;

        selected = { r, c };
        highlightMoves(r, c);

    } else {
        // Clicking same piece again - deselect
        if (selected.r === r && selected.c === c) {
            selected = null;
            clearHighlights();
            return;
        }

        // Clicking own piece - switch selection
        const currentPiece = boardState[selected.r][selected.c];
        if (piece && currentPiece && piece[0] === currentPiece[0]) {
            selected = { r, c };
            highlightMoves(r, c);
            return;
        }

        move(selected.r, selected.c, r, c);
        selected = null;
        clearHighlights();
    }
}

/* ================= MOVE ================= */

function move(fr, fc, tr, tc) {

    fetch(`/api/games/${gameId}/move`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            fromRow: fr,
            fromCol: fc,
            toRow: tr,
            toCol: tc
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Invalid move");
        return res.json();
    })
    .then(() => {
        loadBoard();
        loadHistory();
        loadTurn();
    })
    .catch(err => {
        console.error(err);
        alert("Invalid move!");
    });
}

/* ================= HIGHLIGHT LOGIC ================= */

function highlightMoves(r, c) {
    clearHighlights();

    const squares = document.querySelectorAll(".square");
    const piece = boardState[r][c];

    if (!piece) return;

    const color = piece[0]; // 'w' or 'b'
    squares[r * 8 + c].classList.add("selected");

    const moves = getPossibleMoves(piece, r, c, color);

    moves.forEach(([mr, mc]) => {
        squares[mr * 8 + mc].classList.add("possible");
    });
}

function getPossibleMoves(piece, r, c, color) {
    const type = piece[1]; // p, r, n, b, q, k
    const moves = [];

    switch (type) {
        case 'p': addPawnMoves(moves, r, c, color); break;
        case 'r': addSlidingMoves(moves, r, c, color, [[1,0],[-1,0],[0,1],[0,-1]]); break;
        case 'b': addSlidingMoves(moves, r, c, color, [[1,1],[1,-1],[-1,1],[-1,-1]]); break;
        case 'q': addSlidingMoves(moves, r, c, color, [[1,0],[-1,0],[0,1],[0,-1],[1,1],[1,-1],[-1,1],[-1,-1]]); break;
        case 'n': addKnightMoves(moves, r, c, color); break;
        case 'k': addKingMoves(moves, r, c, color); break;
    }

    return moves;
}

/* --- Pawn --- */
function addPawnMoves(moves, r, c, color) {
    const dir = color === 'w' ? -1 : 1;
    const startRow = color === 'w' ? 6 : 1;

    // Forward 1
    if (inBounds(r + dir, c) && boardState[r + dir][c] === null) {
        moves.push([r + dir, c]);

        // Forward 2 from start
        if (r === startRow && boardState[r + 2 * dir][c] === null) {
            moves.push([r + 2 * dir, c]);
        }
    }

    // Diagonal captures
    for (const dc of [-1, 1]) {
        const nr = r + dir;
        const nc = c + dc;
        if (inBounds(nr, nc)) {
            const target = boardState[nr][nc];
            if (target !== null && target[0] !== color) {
                moves.push([nr, nc]);
            }
        }
    }
}

/* --- Knight --- */
function addKnightMoves(moves, r, c, color) {
    const jumps = [[-2,-1],[-2,1],[-1,-2],[-1,2],[1,-2],[1,2],[2,-1],[2,1]];
    for (const [dr, dc] of jumps) {
        const nr = r + dr;
        const nc = c + dc;
        if (inBounds(nr, nc)) {
            const target = boardState[nr][nc];
            if (target === null || target[0] !== color) {
                moves.push([nr, nc]);
            }
        }
    }
}

/* --- Sliding pieces (Rook, Bishop, Queen) --- */
function addSlidingMoves(moves, r, c, color, directions) {
    for (const [dr, dc] of directions) {
        let nr = r + dr;
        let nc = c + dc;
        while (inBounds(nr, nc)) {
            const target = boardState[nr][nc];
            if (target === null) {
                moves.push([nr, nc]);
            } else {
                if (target[0] !== color) moves.push([nr, nc]); // capture
                break; // blocked
            }
            nr += dr;
            nc += dc;
        }
    }
}

/* --- King --- */
function addKingMoves(moves, r, c, color) {
    const dirs = [[-1,-1],[-1,0],[-1,1],[0,-1],[0,1],[1,-1],[1,0],[1,1]];
    for (const [dr, dc] of dirs) {
        const nr = r + dr;
        const nc = c + dc;
        if (inBounds(nr, nc)) {
            const target = boardState[nr][nc];
            if (target === null || target[0] !== color) {
                moves.push([nr, nc]);
            }
        }
    }
}

/* --- Helper --- */
function inBounds(r, c) {
    return r >= 0 && r < 8 && c >= 0 && c < 8;
}

function clearHighlights() {
    document.querySelectorAll(".square").forEach(sq => {
        sq.classList.remove("selected", "possible");
    });
}

/* ================= HISTORY ================= */

function loadHistory() {

    fetch(`/api/games/${gameId}/history/white`)
    .then(res => res.json())
    .then(list => {
        const div = document.getElementById("whiteHistory");
        div.innerHTML = "";
        list.forEach(m => {
            const li = document.createElement("li");
            li.innerText = m;
            div.appendChild(li);
        });
    });

    fetch(`/api/games/${gameId}/history/black`)
    .then(res => res.json())
    .then(list => {
        const div = document.getElementById("blackHistory");
        div.innerHTML = "";
        list.forEach(m => {
            const li = document.createElement("li");
            li.innerText = m;
            div.appendChild(li);
        });
    });
}

/* ================= TURN ================= */

function loadTurn() {
    fetch(`/api/games/${gameId}/turn`)
    .then(res => res.text())
    .then(t => {
        document.getElementById("turn").innerText = "Turn: " + t;
    });
}

/* ================= RESET ================= */

function resetGame() {
    fetch(`/api/games/${gameId}/reset`, { method: "POST" })
    .then(() => initGame());
}

/* ================= UNDO ================= */

function undoMove() {
    fetch(`/api/games/${gameId}/undo`, { method: "POST" })
    .then(() => initGame());
}