'use strict';
/* ================= STATE ================= */
let gameId = null, selected = null, boardState = [];
let currentUser = null, authHeader = null;
let whiteTime = 600, blackTime = 600;
let timerInterval = null, activeTurn = 'white', gameOver = false;
let SQ = 64; // square size in px - calculated on init

/* ================= SCREEN ================= */
function showScreen(id) {
    ['roleScreen','playerLoginScreen','adminLoginScreen','registerScreen','gameScreen']
        .forEach(s => document.getElementById(s).classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');
    document.getElementById('gameOverModal').classList.add('hidden');
}
function selectRole(r) { showScreen(r === 'admin' ? 'adminLoginScreen' : 'playerLoginScreen'); }

/* ================= BOARD SIZE ================= */
function calcBoardSize() {
    // available space: viewport minus top bar (~58px) minus padding (32px) minus labels (~30px)
    const topBar  = document.querySelector('.top-bar')?.offsetHeight || 58;
    const availH  = window.innerHeight - topBar - 60;  // 60 = padding + labels
    const availW  = window.innerWidth  - 300 - 80;     // 300 = side panel, 80 = gaps+rank col
    const maxSide = Math.min(availH, availW, 640);
    SQ = Math.floor(maxSide / 8);
    if (SQ < 40) SQ = 40;
    if (SQ > 88) SQ = 88;

    // set CSS variable
    document.documentElement.style.setProperty('--sq', SQ + 'px');
    document.getElementById('board').style.width  = (SQ * 8) + 'px';
    document.getElementById('board').style.height = (SQ * 8) + 'px';
}

window.addEventListener('resize', () => { calcBoardSize(); buildCoordinates(); });

/* ================= COORDINATES ================= */
function buildCoordinates() {
    // Rank labels 8→1 on left
    const rankCol = document.getElementById('rankCol');
    rankCol.innerHTML = '';
    ['8','7','6','5','4','3','2','1'].forEach(r => {
        const el = document.createElement('div');
        el.className = 'rank-lbl';
        el.style.height = SQ + 'px';
        el.style.width  = '20px';
        el.innerText = r;
        rankCol.appendChild(el);
    });

    // File labels a→h on bottom
    const fileRow = document.getElementById('fileRow');
    fileRow.innerHTML = '';
    ['a','b','c','d','e','f','g','h'].forEach(f => {
        const el = document.createElement('div');
        el.className = 'file-lbl';
        el.style.width = SQ + 'px';
        el.innerText = f;
        fileRow.appendChild(el);
    });
}

/* ================= SIGN OUT ================= */
function signOut() {
    clearInterval(timerInterval);
    sessionStorage.clear();
    authHeader = null; currentUser = null;
    gameId = null; gameOver = false;
    activeTurn = 'white'; whiteTime = 600; blackTime = 600;
    showScreen('roleScreen');
}

/* ================= REGISTER ================= */
function register() {
    const u = document.getElementById('regUsername').value.trim();
    const p = document.getElementById('regPassword').value.trim();
    const c = document.getElementById('regConfirm').value.trim();
    if (!u || !p) { alert('Username and password required!'); return; }
    if (p !== c)  { alert('Passwords do not match!'); return; }
    if (p.length < 4) { alert('Minimum 4 characters!'); return; }
    fetch('/api/users/register', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: u, password: p, role: 'USER' })
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
    const u = document.getElementById('playerUsername').value.trim();
    const p = document.getElementById('playerPassword').value.trim();
    if (!u || !p) { alert('Username and password required!'); return; }
    authHeader = 'Basic ' + btoa(u + ':' + p);
    currentUser = u;
    fetch('/api/users', { headers: { 'Authorization': authHeader } })
    .then(r => { if (!r.ok) throw new Error('Invalid'); return r.json(); })
    .then(() => {
        sessionStorage.setItem('authHeader', authHeader);
        sessionStorage.setItem('currentUser', u);
        gameOver = false; gameId = null;
        activeTurn = 'white'; whiteTime = 600; blackTime = 600;
        clearInterval(timerInterval);
        document.getElementById('usernameLabel').innerText = '👤 ' + u;
        document.getElementById('whiteTimer').innerText = '10:00';
        document.getElementById('blackTimer').innerText = '10:00';
        showScreen('gameScreen');
        // calculate board size AFTER screen is visible
        setTimeout(() => {
            calcBoardSize();
            buildCoordinates();
            loadRating();
            createGame();
        }, 30);
    })
    .catch(() => alert('Login failed! Check credentials.'));
}

function loginAdmin() {
    const u = document.getElementById('adminUsername').value.trim();
    const p = document.getElementById('adminPassword').value.trim();
    if (!u || !p) { alert('Username and password required!'); return; }
    const ah = 'Basic ' + btoa(u + ':' + p);
    fetch('/api/users', { headers: { 'Authorization': ah } })
    .then(r => { if (!r.ok) throw new Error(); return r.json(); })
    .then(() => {
        sessionStorage.setItem('authHeader', ah);
        sessionStorage.setItem('currentUser', u);
        window.location.href = '/admin.html';
    })
    .catch(() => alert('Login failed!'));
}

/* ================= RATING ================= */
function loadRating() {
    fetch('/api/ratings/me', { headers: { 'Authorization': authHeader } })
    .then(r => r.json())
    .then(d => { document.getElementById('ratingDisplay').innerText = d.rating || 1000; })
    .catch(() => { document.getElementById('ratingDisplay').innerText = '1000'; });
}

/* ================= GAME ================= */
function createGame() {
    fetch('/api/games', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': authHeader }
    })
    .then(r => { if (!r.ok) throw new Error(); return r.json(); })
    .then(game => { gameId = game.id; gameOver = false; initGame(); startTimer(); })
    .catch(err => alert('Failed to create game: ' + err.message));
}

function initGame() { if (!gameId) return; loadBoard(); loadHistory(); loadTurn(); }

/* ================= DRAW BOARD ================= */
function loadBoard() {
    fetch(`/api/games/${gameId}/board`, { headers: { 'Authorization': authHeader } })
    .then(r => r.json())
    .then(board => { boardState = board; drawBoard(board); });
}

function drawBoard(board) {
    const container = document.getElementById('board');
    // IMPORTANT: set explicit size before adding children - prevents reflow
    container.style.width  = (SQ * 8) + 'px';
    container.style.height = (SQ * 8) + 'px';
    container.innerHTML = '';

    const frag = document.createDocumentFragment();
    for (let r = 0; r < 8; r++) {
        for (let c = 0; c < 8; c++) {
            const sq = document.createElement('div');
            sq.className = 'square ' + ((r + c) % 2 === 0 ? 'light' : 'dark');
            // FIXED dimensions - no CSS variable needed on square itself
            sq.style.cssText = `width:${SQ}px;height:${SQ}px;`;
            sq.onclick = () => clickSquare(r, c);

            const piece = board[r][c];
            if (piece) {
                const img = document.createElement('img');
                img.src = '/pieces/' + piece + '.svg';
                img.className = 'piece';
                sq.appendChild(img);
            }
            frag.appendChild(sq);
        }
    }
    container.appendChild(frag);
}

/* ================= CLICK ================= */
function clickSquare(r, c) {
    if (gameOver) return;
    const piece = boardState[r][c];
    if (!selected) {
        if (!piece) return;
        selected = { r, c }; applyHighlights(r, c);
    } else {
        if (selected.r === r && selected.c === c) { selected = null; clearHL(); return; }
        const cur = boardState[selected.r][selected.c];
        if (piece && cur && piece[0] === cur[0]) { selected = { r, c }; applyHighlights(r, c); return; }
        doMove(selected.r, selected.c, r, c);
        selected = null; clearHL();
    }
}

/* ================= HIGHLIGHT ================= */
function applyHighlights(r, c) {
    clearHL();
    const squares = document.querySelectorAll('.square');
    const piece = boardState[r][c];
    if (!piece) return;
    squares[r * 8 + c].classList.add('selected');
    getPossibleMoves(piece, r, c, piece[0]).forEach(([mr, mc]) => {
        const sq = squares[mr * 8 + mc];
        if (boardState[mr][mc]) sq.classList.add('capture-ring');
        else                     sq.classList.add('dot');
    });
}

function clearHL() {
    document.querySelectorAll('.square').forEach(s => s.classList.remove('selected','dot','capture-ring'));
}

function getPossibleMoves(piece, r, c, color) {
    const m = [];
    switch(piece[1]) {
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

/* ================= MOVE ================= */
function doMove(fr, fc, tr, tc) {
    fetch(`/api/games/${gameId}/move`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': authHeader },
        body: JSON.stringify({ fromRow: fr, fromCol: fc, toRow: tr, toCol: tc })
    })
    .then(r => {
        if (!r.ok) return r.text().then(() => { throw new Error('INVALID'); });
        return r.text();
    })
    .then(raw => {
        const result = raw.replace(/['"]/g,'').trim();
        // update board state directly from response - avoid full reload flicker
        loadBoard(); loadHistory(); loadTurn(); switchTimer();
        if (result === 'WHITE_WIN' || result === 'BLACK_WIN' || result === 'STALEMATE') {
            gameOver = true; clearInterval(timerInterval);
            showModal(result, '');
        }
    })
    .catch(err => { if (err.message === 'INVALID') alert('Invalid move!'); });
}

/* ================= MODAL ================= */
function showModal(result, overrideMsg) {
    let icon, title, msg;
    if      (result==='WHITE_WIN') { icon='♔'; title='White Wins!'; msg=overrideMsg||'♔ White wins by Checkmate!'; updateRating('white'); }
    else if (result==='BLACK_WIN') { icon='♚'; title='Black Wins!'; msg=overrideMsg||'♚ Black wins by Checkmate!'; updateRating('black'); }
    else if (result==='STALEMATE') { icon='🤝'; title='Stalemate!'; msg=overrideMsg||"No legal moves — it's a draw!"; updateRating('draw'); }
    else return;
    document.getElementById('resultIcon').innerText  = icon;
    document.getElementById('resultTitle').innerText = title;
    document.getElementById('resultMsg').innerText   = msg;
    document.getElementById('ratingChange').innerText = 'Updating rating...';
    document.getElementById('ratingChange').className = 'rating-change';
    document.getElementById('gameOverModal').classList.remove('hidden');
}

function updateRating(winner) {
    fetch(`/api/ratings/update-solo?username=${currentUser}&result=${winner}`, {
        method: 'POST', headers: { 'Authorization': authHeader }
    })
    .then(r => r.json())
    .then(data => {
        if (!data || data.newRating === undefined) return;
        const ch = data.ratingChange || 0;
        const el = document.getElementById('ratingChange');
        el.innerText  = ch >= 0 ? `Rating: +${ch} ⭐` : `Rating: ${ch} ⭐`;
        el.className  = 'rating-change' + (ch < 0 ? ' negative' : '');
        document.getElementById('ratingDisplay').innerText = data.newRating;
    })
    .catch(() => { document.getElementById('ratingChange').innerText = ''; });
}

/* ================= HISTORY & TURN ================= */
function loadHistory() {
    ['white','black'].forEach(color => {
        fetch(`/api/games/${gameId}/history/${color}`, { headers: { 'Authorization': authHeader } })
        .then(r => r.json()).then(list => {
            const ul = document.getElementById(color + 'History');
            ul.innerHTML = '';
            list.forEach(m => { const li = document.createElement('li'); li.innerText = m; ul.appendChild(li); });
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
        document.getElementById('turn').innerText = 'Turn: ' + turn.charAt(0).toUpperCase() + turn.slice(1);
        document.getElementById('whiteTimerRow').className = 'timer-row'+(turn==='white'?' active':'');
        document.getElementById('blackTimerRow').className = 'timer-row'+(turn==='black'?' active':'');
        document.getElementById('whiteLabel').className = 'player-label'+(turn==='white'?' active':'');
        document.getElementById('blackLabel').className = 'player-label'+(turn==='black'?' active':'');
    });
}

/* ================= TIMER ================= */
function startTimer() {
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        if (gameOver) return;
        if (activeTurn === 'white') {
            whiteTime--;
            document.getElementById('whiteTimer').innerText = fmt(whiteTime);
            if (whiteTime <= 30) document.getElementById('whiteTimerRow').classList.add('low');
            if (whiteTime <= 0)  { clearInterval(timerInterval); handleTimeout('white'); }
        } else {
            blackTime--;
            document.getElementById('blackTimer').innerText = fmt(blackTime);
            if (blackTime <= 30) document.getElementById('blackTimerRow').classList.add('low');
            if (blackTime <= 0)  { clearInterval(timerInterval); handleTimeout('black'); }
        }
    }, 1000);
}

function handleTimeout(loser) {
    gameOver = true;
    const winner = loser === 'white' ? 'black' : 'white';
    fetch(`/api/games/${gameId}/timeout?loserColor=${loser}`, {
        method: 'POST', headers: { 'Authorization': authHeader }
    }).then(() => {
        const res = winner === 'white' ? 'WHITE_WIN' : 'BLACK_WIN';
        const L = loser.charAt(0).toUpperCase()+loser.slice(1);
        const W = winner.charAt(0).toUpperCase()+winner.slice(1);
        showModal(res, `${L} ran out of time! ${W} wins!`);
    });
}

function switchTimer() { activeTurn = activeTurn === 'white' ? 'black' : 'white'; }
function fmt(s){return`${Math.floor(s/60).toString().padStart(2,'0')}:${(s%60).toString().padStart(2,'0')}`;}

/* ================= RESET & UNDO ================= */
function resetGame() {
    clearInterval(timerInterval);
    fetch(`/api/games/${gameId}/reset`, { method: 'POST', headers: { 'Authorization': authHeader } })
    .then(() => {
        gameOver=false; whiteTime=600; blackTime=600; activeTurn='white';
        document.getElementById('whiteTimer').innerText='10:00';
        document.getElementById('blackTimer').innerText='10:00';
        document.getElementById('whiteTimerRow').classList.remove('low');
        document.getElementById('blackTimerRow').classList.remove('low');
        document.getElementById('gameOverModal').classList.add('hidden');
        initGame(); startTimer();
    });
}

function undoMove() {
    if (gameOver) { gameOver=false; document.getElementById('gameOverModal').classList.add('hidden'); }
    fetch(`/api/games/${gameId}/undo`, { method:'POST', headers:{'Authorization':authHeader} })
    .then(() => initGame());
}