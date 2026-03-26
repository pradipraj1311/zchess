/* ================= GLOBAL STATE ================= */
let gameId = null, selected = null, boardState = [];
let currentUser = null, authHeader = null;
let whiteTime = 600, blackTime = 600, timerInterval = null, currentTurn = "white";

/* ================= AUTH ================= */

function goBack() {
    document.getElementById("loginScreen").classList.add("hidden");
    document.getElementById("roleScreen").classList.remove("hidden");
}

function selectRole(role) {
    document.getElementById("roleScreen").classList.add("hidden");
    document.getElementById("loginScreen").classList.remove("hidden");
    document.getElementById("loginScreen").dataset.role = role;

    const isAdmin = role === 'admin';
    document.getElementById("loginTitle").innerText = isAdmin ? "Admin Login" : "Player Login";
    document.getElementById("registerBtn").style.display = isAdmin ? "none" : "block";
}

function register() {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    if (!username || !password) { alert("Username and password required!"); return; }

    fetch("/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password, role: "USER" })
    })
    .then(res => { if (!res.ok) throw new Error("Failed"); return res.json(); })
    .then(() => alert("Registered! Please login."))
    .catch(() => alert("Registration failed! Username may already exist."));
}

function login() {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const role = document.getElementById("loginScreen").dataset.role || 'player';
    if (!username || !password) { alert("Username and password required!"); return; }

    authHeader = "Basic " + btoa(username + ":" + password);
    currentUser = username;

    fetch("/api/users", { headers: { "Authorization": authHeader } })
    .then(res => { if (!res.ok) throw new Error("Invalid"); return res.json(); })
    .then(() => {
        sessionStorage.setItem("authHeader", authHeader);
        sessionStorage.setItem("currentUser", username);
        document.getElementById("loginScreen").classList.add("hidden");

        if (role === 'admin') {
            window.location.href = "/admin.html";
            return;
        }

        document.getElementById("gameScreen").classList.remove("hidden");
        document.getElementById("usernameLabel").innerText = username;
        loadRating();
        createGame();
    })
    .catch(() => alert("Login failed! Check credentials."));
}

/* ================= RATING ================= */

function loadRating() {
    fetch("/api/ratings/me", { headers: { "Authorization": authHeader } })
    .then(res => res.json())
    .then(data => { document.getElementById("ratingDisplay").innerText = data.rating || 1000; })
    .catch(() => { document.getElementById("ratingDisplay").innerText = "1000"; });
}

/* ================= GAME INIT ================= */

function createGame() {
    fetch("/api/games", {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": authHeader }
    })
    .then(res => { if (!res.ok) throw new Error("Failed"); return res.json(); })
    .then(game => { gameId = game.id; initGame(); startTimer(); })
    .catch(err => alert("Failed to create game: " + err.message));
}

function initGame() {
    if (!gameId) return;
    loadBoard(); loadHistory(); loadTurn();
}

/* ================= BOARD ================= */

function loadBoard() {
    fetch(`/api/games/${gameId}/board`, { headers: { "Authorization": authHeader } })
    .then(res => res.json())
    .then(board => { boardState = board; drawBoard(board); });
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

/* ================= CLICK & MOVE ================= */

function clickSquare(r, c) {
    const piece = boardState[r][c];
    if (!selected) {
        if (!piece) return;
        selected = { r, c }; highlightMoves(r, c);
    } else {
        if (selected.r === r && selected.c === c) { selected = null; clearHighlights(); return; }
        const cur = boardState[selected.r][selected.c];
        if (piece && cur && piece[0] === cur[0]) { selected = { r, c }; highlightMoves(r, c); return; }
        move(selected.r, selected.c, r, c);
        selected = null; clearHighlights();
    }
}

function move(fr, fc, tr, tc) {
    fetch(`/api/games/${gameId}/move`, {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": authHeader },
        body: JSON.stringify({ fromRow: fr, fromCol: fc, toRow: tr, toCol: tc })
    })
    .then(res => { if (!res.ok) throw new Error("Invalid"); return res.json(); })
    .then(() => { loadBoard(); loadHistory(); loadTurn(); switchTimer(); })
    .catch(() => alert("Invalid move!"));
}

/* ================= HIGHLIGHT ================= */

function highlightMoves(r, c) {
    clearHighlights();
    const squares = document.querySelectorAll(".square");
    const piece = boardState[r][c];
    if (!piece) return;
    squares[r * 8 + c].classList.add("selected");
    getPossibleMoves(piece, r, c, piece[0]).forEach(([mr, mc]) => squares[mr * 8 + mc].classList.add("possible"));
}

function getPossibleMoves(piece, r, c, color) {
    const moves = [];
    switch (piece[1]) {
        case 'p': addPawnMoves(moves, r, c, color); break;
        case 'r': addSliding(moves, r, c, color, [[1,0],[-1,0],[0,1],[0,-1]]); break;
        case 'b': addSliding(moves, r, c, color, [[1,1],[1,-1],[-1,1],[-1,-1]]); break;
        case 'q': addSliding(moves, r, c, color, [[1,0],[-1,0],[0,1],[0,-1],[1,1],[1,-1],[-1,1],[-1,-1]]); break;
        case 'n': addKnight(moves, r, c, color); break;
        case 'k': addKing(moves, r, c, color); break;
    }
    return moves;
}

function addPawnMoves(m, r, c, color) {
    const dir = color === 'w' ? -1 : 1, start = color === 'w' ? 6 : 1;
    if (inB(r+dir,c) && !boardState[r+dir][c]) {
        m.push([r+dir,c]);
        if (r===start && !boardState[r+2*dir][c]) m.push([r+2*dir,c]);
    }
    for (const dc of [-1,1])
        if (inB(r+dir,c+dc) && boardState[r+dir][c+dc] && boardState[r+dir][c+dc][0]!==color)
            m.push([r+dir,c+dc]);
}

function addKnight(m, r, c, color) {
    for (const [dr,dc] of [[-2,-1],[-2,1],[-1,-2],[-1,2],[1,-2],[1,2],[2,-1],[2,1]])
        if (inB(r+dr,c+dc) && (!boardState[r+dr][c+dc] || boardState[r+dr][c+dc][0]!==color))
            m.push([r+dr,c+dc]);
}

function addSliding(m, r, c, color, dirs) {
    for (const [dr,dc] of dirs) {
        let nr=r+dr, nc=c+dc;
        while (inB(nr,nc)) {
            if (!boardState[nr][nc]) { m.push([nr,nc]); }
            else { if (boardState[nr][nc][0]!==color) m.push([nr,nc]); break; }
            nr+=dr; nc+=dc;
        }
    }
}

function addKing(m, r, c, color) {
    for (const [dr,dc] of [[-1,-1],[-1,0],[-1,1],[0,-1],[0,1],[1,-1],[1,0],[1,1]])
        if (inB(r+dr,c+dc) && (!boardState[r+dr][c+dc] || boardState[r+dr][c+dc][0]!==color))
            m.push([r+dr,c+dc]);
}

function inB(r,c) { return r>=0 && r<8 && c>=0 && c<8; }

function clearHighlights() {
    document.querySelectorAll(".square").forEach(s => s.classList.remove("selected","possible"));
}

/* ================= HISTORY & TURN ================= */

function loadHistory() {
    ["white","black"].forEach(color => {
        fetch(`/api/games/${gameId}/history/${color}`, { headers: { "Authorization": authHeader } })
        .then(r => r.json()).then(list => {
            const ul = document.getElementById(color + "History");
            ul.innerHTML = "";
            list.forEach(m => { const li = document.createElement("li"); li.innerText = m; ul.appendChild(li); });
        });
    });
}

function loadTurn() {
    fetch(`/api/games/${gameId}/turn`, { headers: { "Authorization": authHeader } })
    .then(r => r.text()).then(t => { document.getElementById("turn").innerText = "Turn: " + t; });
}

/* ================= TIMER ================= */

function startTimer() {
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        if (currentTurn === "white") {
            whiteTime--;
            document.getElementById("whiteTimer").innerText = fmt(whiteTime);
            if (whiteTime <= 0) { clearInterval(timerInterval); alert("White time up! Black wins!"); }
        } else {
            blackTime--;
            document.getElementById("blackTimer").innerText = fmt(blackTime);
            if (blackTime <= 0) { clearInterval(timerInterval); alert("Black time up! White wins!"); }
        }
    }, 1000);
}

function switchTimer() { currentTurn = currentTurn === "white" ? "black" : "white"; }
function fmt(s) { return `${Math.floor(s/60).toString().padStart(2,'0')}:${(s%60).toString().padStart(2,'0')}`; }

/* ================= RESET & UNDO ================= */

function resetGame() {
    fetch(`/api/games/${gameId}/reset`, { method: "POST", headers: { "Authorization": authHeader } })
    .then(() => {
        whiteTime=600; blackTime=600; currentTurn="white";
        document.getElementById("whiteTimer").innerText = "10:00";
        document.getElementById("blackTimer").innerText = "10:00";
        initGame(); startTimer();
    });
}

function undoMove() {
    fetch(`/api/games/${gameId}/undo`, { method: "POST", headers: { "Authorization": authHeader } })
    .then(() => initGame());
}