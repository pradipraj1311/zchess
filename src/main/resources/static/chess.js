document.addEventListener("DOMContentLoaded", function () {

    const boardDiv = document.getElementById("board");
    const turnText = document.getElementById("turn");

    let selected = null;

    let whiteTime = 600;
    let blackTime = 600;

    let currentTurn = "white";
    let timerInterval;

    /* TIMER */

    timerInterval = setInterval(() => {

        if (currentTurn === "white") whiteTime--;
        else blackTime--;

        updateTimer();

        if (whiteTime <= 0) {
            clearInterval(timerInterval);
            alert("Black Wins");
        }

        if (blackTime <= 0) {
            clearInterval(timerInterval);
            alert("White Wins");
        }

    }, 1000);

    function updateTimer() {
        document.getElementById("whiteTimer").innerText = formatTime(whiteTime);
        document.getElementById("blackTimer").innerText = formatTime(blackTime);
    }

    function formatTime(t) {
        let m = Math.floor(t / 60);
        let s = t % 60;
        return m + ":" + (s < 10 ? "0" : "") + s;
    }

    /* BOARD */

    function loadBoard() {
        fetch("/api/game/board")
            .then(res => res.json())
            .then(board => drawBoard(board));
    }

    function drawBoard(board) {

        boardDiv.innerHTML = "";

        for (let r = 0; r < 8; r++) {
            for (let c = 0; c < 8; c++) {

                const sq = document.createElement("div");
                sq.className = "square";

                if ((r + c) % 2 === 0) sq.classList.add("light");
                else sq.classList.add("dark");

                sq.dataset.row = r;
                sq.dataset.col = c;

                sq.onclick = squareClick;

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

    /* CLICK MOVE */

    function squareClick() {

        const r = this.dataset.row;
        const c = this.dataset.col;

        if (!selected) {

            if (!this.querySelector("img")) return;

            clearSelection();

            selected = { r, c };
            this.classList.add("highlight");

        } else {

            movePiece(selected.r, selected.c, r, c);
            clearSelection();
            selected = null;
        }
    }

    function clearSelection() {
        document.querySelectorAll(".square").forEach(sq => {
            sq.classList.remove("highlight");
        });
    }

    /* MOVE */

    function movePiece(fr, fc, tr, tc) {

        fetch("/api/move", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                fromRow: parseInt(fr),
                fromCol: parseInt(fc),
                toRow: parseInt(tr),
                toCol: parseInt(tc)
            })
        })
        .then(res => {
            if (!res.ok) throw new Error("Invalid move ❌");
            return res.json();
        })
        .then(() => {
            loadBoard();
            loadHistory();
            loadTurn();
        })
        .catch(err => alert(err.message));
    }

    /* HISTORY */

    function loadHistory() {

        fetch("/api/game/history/white")
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

        fetch("/api/game/history/black")
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

    /* TURN */

    function loadTurn() {
        fetch("/api/game/turn")
            .then(res => res.text())
            .then(t => {
                currentTurn = t;
                turnText.innerText = "Turn: " + t;
            });
    }

    /* RESET */

    window.resetGame = function () {
        fetch("/api/game/reset", {method: "POST"})
            .then(() => {
                loadBoard();
                loadHistory();
                loadTurn();
            });
    };

    /* UNDO */

    window.undoMove = function () {
        fetch("/api/game/undo", {method: "POST"})
            .then(() => {
                loadBoard();
                loadHistory();
                loadTurn();
            });
    };

    /* INIT */

    loadBoard();
    loadHistory();
    loadTurn();

});