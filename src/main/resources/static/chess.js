/* ================= GLOBAL STATE ================= */
let gameId = null, selected = null, boardState = [];
let currentUser = null, authHeader = null;
let whiteTime = 600, blackTime = 600;
let timerInterval = null, activeTurn = "white";
let gameOver = false;

/* ================= SCREEN ================= */
function showScreen(id) {
    ['roleScreen','playerLoginScreen','adminLoginScreen','registerScreen','gameScreen']
        .forEach(s => document.getElementById(s).classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');
    document.getElementById('gameOverModal').classList.add('hidden');
}

function selectRole(role) {
    showScreen(role === 'admin' ? 'adminLoginScreen' : 'playerLoginScreen');
}

/* ================= REGISTER ================= */
function register() {
    const username = document.getElementById('regUsername').value.trim();
    const password = document.getElementById('regPassword').value.trim();
    const confirm  = document.getElementById('regConfirm').value.trim();

    if (!username || !password) { alert('Username and password required!'); return; }
    if (password !== confirm)   { alert('Passwords do not match!'); return; }
    if (password.length < 4)    { alert('Minimum 4 characters!'); return; }

    fetch('/api/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, role: 'USER' })
    })
    .then(r => { if (!r.ok) throw new Error(); return r.json(); })
    .then(() => {
        alert('Account created! Please login.');
        ['regUsername','regPassword','regConfirm'].forEach(id => document.getElementById(id).value = '');
        showScreen('playerLoginScreen');
    })
    .catch(() => alert('Registration failed! Username may already exist.'));
}

/* ================= LOGIN ================= */
function login() {
    const username = document.getElementById('playerUsername').value.trim();
    const password = document.getElementById('playerPassword').value.trim();
    if (!username || !password) { alert('Username and password required!'); return; }

    authHeader  = 'Basic ' + btoa(username + ':' + password);
    currentUser = username;

    fetch('/api/users', { headers: { 'Authorization': authHeader } })
    .then(r => { if (!r.ok) throw new Error('Invalid'); return r.json(); })
    .then(() => {
        sessionStorage.setItem('authHeader', authHeader);
        sessionStorage.setItem('currentUser', username);
        document.getElementById('usernameLabel').innerText = '👤 ' + username;
        loadRating();
        createGame();
        showScreen('gameScreen');
    })
    .catch(() => alert('Login failed! Check credentials.'));
}

function loginAdmin() {
    const username = document.getElementById('adminUsername').value.trim();
    const password = document.getElementById('adminPassword').value.trim();
    if (!username || !password) { alert('Username and password required!'); return; }

    const ah = 'Basic ' + btoa(username + ':' + password);

    fetch('/api/users', { headers: { 'Authorization': ah } })
    .then(r => { if (!r.ok) throw new Error('Invalid'); return r.json(); })
    .then(() => {
        sessionStorage.setItem('authHeader', ah);
        sessionStorage.setItem('currentUser', username);
        window.location.href = '/admin.html';
    })
    .catch(() => alert('Login failed! Check credentials.'));
}

/* ================= RATING ================= */
function loadRating() {
    fetch('/api/ratings/me', { headers: { 'Authorization': authHeader } })
    .then(r => r.json())
    .then(d => { document.getElementById('ratingDisplay').innerText = d.rating || 1000; })
    .catch(() => { document.getElementById('ratingDisplay').innerText = '1000'; });
}

/* ================= GAME INIT ================= */
function createGame() {
    gameOver = false;
    activeTurn = 'white';
    fetch('/api/games', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': authHeader }
    })
    .then(r => { if (!r.ok) throw new Error(); return r.json(); })
    .then(game => { gameId = game.id; initGame(); startTimer(); })
    .catch(err => alert('Failed to create game: ' + err.message));
}

function initGame() {
    if (!gameId) return;
    loadBoard(); loadHistory(); loadTurn();
}

/* ================= BOARD ================= */
function loadBoard() {
    fetch(`/api/games/${gameId}/board`, { headers: { 'Authorization': authHeader } })
    .then(r => r.json())
    .then(board => { boardState = board; drawBoard(board); });
}

function drawBoard(board) {
    const div = document.getElementById('board');
    div.innerHTML = '';
    for (let r = 0; r < 8; r++) {
        for (let c = 0; c < 8; c++) {
            const sq = document.createElement('div');
            sq.className = 'square ' + ((r + c) % 2 === 0 ? 'light' : 'dark');
            sq.dataset.r = r; sq.dataset.c = c;
            sq.onclick = () => clickSquare(r, c);
            const piece = board[r][c];
            if (piece) {
                const img = document.createElement('img');
                img.src = '/pieces/' + piece + '.svg';
                img.className = 'piece';
                sq.appendChild(img);
            }
            div.appendChild(sq);
        }
    }
}

/* ================= CLICK & MOVE ================= */
function clickSquare(r, c) {
    if (gameOver) return;
    const piece = boardState[r][c];
    if (!selected) {
        if (!piece) return;
        selected = { r, c }; highlightMoves(r, c);
    } else {
        if (selected.r === r && selected.c === c) { selected = null; clearHighlights(); return; }
        const cur = boardState[selected.r][selected.c];
        if (piece && cur && piece[0] === cur[0]) { selected = { r, c }; highlightMoves(r, c); return; }
        doMove(selected.r, selected.c, r, c);
        selected = null; clearHighlights();
    }
}

function doMove(fr, fc, tr, tc) {
    fetch(`/api/games/${gameId}/move`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': authHeader },
        body: JSON.stringify({ fromRow: fr, fromCol: fc, toRow: tr, toCol: tc })
    })
    .then(r => {
        if (!r.ok) {
            // 400 = invalid move - just show alert, no modal
            return r.text().then(() => { throw new Error('INVALID'); });
        }
        return r.text(); // get raw text - avoid JSON parse issues
    })
    .then(raw => {
        // clean the result string - remove quotes if any
        const result = raw.replace(/['"]/g, '').trim();
        loadBoard();
        loadHistory();
        loadTurn();
        switchTimer();

        // only show modal on actual game-ending results
        if (result === 'WHITE_WIN' || result === 'BLACK_WIN' || result === 'STALEMATE') {
            gameOver = true;
            clearInterval(timerInterval);
            handleGameResult(result);
        }
        // if "OK" or anything else - continue normally
    })
    .catch(err => {
        if (err.message === 'INVALID') alert('Invalid move!');
    });
}

/* ================= GAME RESULT MODAL ================= */
function handleGameResult(result) {
    let icon, title, msg;

    if (result === 'WHITE_WIN') {
        icon = '♔'; title = 'White Wins!'; msg = 'Checkmate! ♔ White wins the game.';
        updateRating('white');
    } else if (result === 'BLACK_WIN') {
        icon = '♚'; title = 'Black Wins!'; msg = 'Checkmate! ♚ Black wins the game.';
        updateRating('black');
    } else if (result === 'STALEMATE') {
        icon = '🤝'; title = 'Stalemate!'; msg = "No legal moves left. It's a draw!";
        updateRating('draw');
    } else {
        return; // unknown result - do nothing
    }

    document.getElementById('resultIcon').innerText  = icon;
    document.getElementById('resultTitle').innerText = title;
    document.getElementById('resultMsg').innerText   = msg;
    document.getElementById('ratingChange').innerText = '';
    document.getElementById('gameOverModal').classList.remove('hidden');
}

/* ================= RATING UPDATE ================= */
function updateRating(winner) {
    fetch(`/api/ratings/update-solo?username=${currentUser}&result=${winner}`, {
        method: 'POST',
        headers: { 'Authorization': authHeader }
    })
    .then(r => r.json())
    .then(data => {
        if (!data || data.newRating === undefined) return;
        const change = data.ratingChange || 0;
        const el = document.getElementById('ratingChange');
        el.innerText  = change >= 0 ? `Rating: +${change} ⭐` : `Rating: ${change} ⭐`;
        el.className  = 'rating-change' + (change < 0 ? ' negative' : '');
        document.getElementById('ratingDisplay').innerText = data.newRating;
    })
    .catch(() => {});
}

/* ================= HIGHLIGHT ================= */
function highlightMoves(r, c) {
    clearHighlights();
    const squares = document.querySelectorAll('.square');
    const piece = boardState[r][c];
    if (!piece) return;
    squares[r * 8 + c].classList.add('selected');
    getPossibleMoves(piece, r, c, piece[0]).forEach(([mr, mc]) => {
        const sq = squares[mr * 8 + mc];
        sq.classList.add('possible');
        if (boardState[mr][mc]) sq.classList.add('has-piece');
    });
}

function getPossibleMoves(piece, r, c, color) {
    const m = [];
    switch (piece[1]) {
        case 'p': addPawn(m,r,c,color); break;
        case 'r': addSlide(m,r,c,color,[[1,0],[-1,0],[0,1],[0,-1]]); break;
        case 'b': addSlide(m,r,c,color,[[1,1],[1,-1],[-1,1],[-1,-1]]); break;
        case 'q': addSlide(m,r,c,color,[[1,0],[-1,0],[0,1],[0,-1],[1,1],[1,-1],[-1,1],[-1,-1]]); break;
        case 'n': addKnight(m,r,c,color); break;
        case 'k': addKing(m,r,c,color); break;
    }
    return m;
}

function addPawn(m,r,c,col){const d=col==='w'?-1:1,s=col==='w'?6:1;if(inB(r+d,c)&&!boardState[r+d][c]){m.push([r+d,c]);if(r===s&&!boardState[r+2*d][c])m.push([r+2*d,c]);}for(const dc of[-1,1])if(inB(r+d,c+dc)&&boardState[r+d][c+dc]&&boardState[r+d][c+dc][0]!==col)m.push([r+d,c+dc]);}
function addKnight(m,r,c,col){for(const[dr,dc]of[[-2,-1],[-2,1],[-1,-2],[-1,2],[1,-2],[1,2],[2,-1],[2,1]])if(inB(r+dr,c+dc)&&(!boardState[r+dr][c+dc]||boardState[r+dr][c+dc][0]!==col))m.push([r+dr,c+dc]);}
function addSlide(m,r,c,col,dirs){for(const[dr,dc]of dirs){let nr=r+dr,nc=c+dc;while(inB(nr,nc)){if(!boardState[nr][nc]){m.push([nr,nc]);}else{if(boardState[nr][nc][0]!==col)m.push([nr,nc]);break;}nr+=dr;nc+=dc;}}}
function addKing(m,r,c,col){for(const[dr,dc]of[[-1,-1],[-1,0],[-1,1],[0,-1],[0,1],[1,-1],[1,0],[1,1]])if(inB(r+dr,c+dc)&&(!boardState[r+dr][c+dc]||boardState[r+dr][c+dc][0]!==col))m.push([r+dr,c+dc]);}
function inB(r,c){return r>=0&&r<8&&c>=0&&c<8;}

function clearHighlights(){
    document.querySelectorAll('.square').forEach(s => s.classList.remove('selected','possible','has-piece'));
}

/* ================= HISTORY & TURN ================= */
function loadHistory() {
    ['white','black'].forEach(color => {
        fetch(`/api/games/${gameId}/history/${color}`, { headers: { 'Authorization': authHeader } })
        .then(r => r.json()).then(list => {
            const ul = document.getElementById(color + 'History');
            ul.innerHTML = '';
            list.forEach(m => {
                const li = document.createElement('li');
                li.innerText = m;
                ul.appendChild(li);
            });
            ul.scrollTop = ul.scrollHeight;
        });
    });
}

function loadTurn() {
    fetch(`/api/games/${gameId}/turn`, { headers: { 'Authorization': authHeader } })
    .then(r => r.text())
    .then(raw => {
        const turn = raw.replace(/['"]/g,'').trim();
        activeTurn = turn;
        document.getElementById('turn').innerText = 'Turn: ' + turn;

        // timer row highlight
        document.getElementById('whiteTimerRow').className = 'timer-row' + (turn==='white' ? ' active' : '');
        document.getElementById('blackTimerRow').className = 'timer-row' + (turn==='black' ? ' active' : '');

        // player label highlight
        document.getElementById('whiteLabel').className = 'player-label' + (turn==='white' ? ' active' : '');
        document.getElementById('blackLabel').className = 'player-label' + (turn==='black' ? ' active' : '');
    });
}

/* ================= TIMER ================= */
function startTimer() {
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        if (gameOver) return;
        if (activeTurn === 'white') {
            whiteTime--;
            const el = document.getElementById('whiteTimer');
            el.innerText = fmt(whiteTime);
            if (whiteTime <= 30) el.parentElement.classList.add('low');
            if (whiteTime <= 0) handleTimeout('white');
        } else {
            blackTime--;
            const el = document.getElementById('blackTimer');
            el.innerText = fmt(blackTime);
            if (blackTime <= 30) el.parentElement.classList.add('low');
            if (blackTime <= 0) handleTimeout('black');
        }
    }, 1000);
}

function handleTimeout(loserColor) {
    clearInterval(timerInterval);
    gameOver = true;
    const winner = loserColor === 'white' ? 'black' : 'white';

    fetch(`/api/games/${gameId}/timeout?loserColor=${loserColor}`, {
        method: 'POST', headers: { 'Authorization': authHeader }
    })
    .then(() => {
        const icon = winner === 'white' ? '♔' : '♚';
        document.getElementById('resultIcon').innerText  = icon;
        document.getElementById('resultTitle').innerText = `${winner.charAt(0).toUpperCase()+winner.slice(1)} Wins!`;
        document.getElementById('resultMsg').innerText   = `${loserColor.charAt(0).toUpperCase()+loserColor.slice(1)} ran out of time!`;
        document.getElementById('ratingChange').innerText = '';
        document.getElementById('gameOverModal').classList.remove('hidden');
        updateRating(winner);
    });
}

function switchTimer() { activeTurn = activeTurn === 'white' ? 'black' : 'white'; }
function fmt(s){ return `${Math.floor(s/60).toString().padStart(2,'0')}:${(s%60).toString().padStart(2,'0')}`; }

/* ================= RESET & UNDO ================= */
function resetGame() {
    fetch(`/api/games/${gameId}/reset`, { method: 'POST', headers: { 'Authorization': authHeader } })
    .then(() => {
        gameOver = false;
        whiteTime = 600; blackTime = 600; activeTurn = 'white';
        document.getElementById('whiteTimer').innerText = '10:00';
        document.getElementById('blackTimer').innerText = '10:00';
        document.getElementById('whiteTimer').parentElement.classList.remove('low');
        document.getElementById('blackTimer').parentElement.classList.remove('low');
        document.getElementById('gameOverModal').classList.add('hidden');
        initGame();
        startTimer();
    });
}

function undoMove() {
    if (gameOver) {
        gameOver = false;
        document.getElementById('gameOverModal').classList.add('hidden');
    }
    fetch(`/api/games/${gameId}/undo`, { method:'POST', headers:{'Authorization':authHeader} })
    .then(() => { initGame(); });
}