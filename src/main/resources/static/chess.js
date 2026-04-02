'use strict';
/* ================= STATE ================= */
let gameId = null, selected = null, boardState = [];
let currentUser = null, authHeader = null;
let whiteTime = 600, blackTime = 600;
let timerInterval = null, activeTurn = 'white', gameOver = false;
let SQ = 64;

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
    const topBar = document.querySelector('.top-bar')?.offsetHeight || 58;
    const turnH  = document.querySelector('.turn-banner')?.offsetHeight || 44;
    const availH = window.innerHeight - topBar - turnH - 80;
    const availW = window.innerWidth  - 300 - 80;
    const maxSide = Math.min(availH, availW, 660);
    SQ = Math.max(40, Math.min(88, Math.floor(maxSide / 8)));
    document.getElementById('board').style.width  = (SQ * 8) + 'px';
    document.getElementById('board').style.height = (SQ * 8) + 'px';
}
window.addEventListener('resize', () => { calcBoardSize(); buildCoords(); });

/* ================= COORDINATES ================= */
function buildCoords() {
    const rc = document.getElementById('rankCol');
    rc.innerHTML = '';
    ['8','7','6','5','4','3','2','1'].forEach(r => {
        const el = document.createElement('div');
        el.className = 'rank-lbl';
        el.style.cssText = `height:${SQ}px;width:20px;`;
        el.innerText = r;
        rc.appendChild(el);
    });
    const fr = document.getElementById('fileRow');
    fr.innerHTML = '';
    ['a','b','c','d','e','f','g','h'].forEach(f => {
        const el = document.createElement('div');
        el.className = 'file-lbl';
        el.style.cssText = `width:${SQ}px;`;
        el.innerText = f;
        fr.appendChild(el);
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
        setTimeout(() => { calcBoardSize(); buildCoords(); loadRating(); createGame(); }, 50);
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
        method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': authHeader }
    })
    .then(r => { if (!r.ok) throw new Error(); return r.json(); })
    .then(game => { gameId = game.id; gameOver = false; initGame(); startTimer(); })
    .catch(err => alert('Failed to create game: ' + err.message));
}

function initGame() { if (!gameId) return; loadBoard(); loadHistory(); loadTurn(); }

/* ================= BOARD ================= */
function loadBoard() {
    fetch(`/api/games/${gameId}/board`, { headers: { 'Authorization': authHeader } })
    .then(r => r.json())
    .then(board => { boardState = board; drawBoard(board); });
}

function drawBoard(board) {
    const container = document.getElementById('board');
    container.style.width  = (SQ * 8) + 'px';
    container.style.height = (SQ * 8) + 'px';
    container.innerHTML = '';
    const frag = document.createDocumentFragment();
    for (let r = 0; r < 8; r++) {
        for (let c = 0; c < 8; c++) {
            const sq = document.createElement('div');
            sq.className = 'square ' + ((r + c) % 2 === 0 ? 'light' : 'dark');
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
        selected = { r, c }; applyHL(r, c);
    } else {
        if (selected.r === r && selected.c === c) { selected = null; clearHL(); return; }
        const cur = boardState[selected.r][selected.c];
        if (piece && cur && piece[0] === cur[0]) { selected = { r, c }; applyHL(r, c); return; }
        doMove(selected.r, selected.c, r, c);
        selected = null; clearHL();
    }
}

/* ================= HIGHLIGHT ================= */
function applyHL(r, c) {
    clearHL();
    const sqs = document.querySelectorAll('.square');
    const piece = boardState[r][c];
    if (!piece) return;
    sqs[r * 8 + c].classList.add('selected');
    getMoves(piece, r, c, piece[0]).forEach(([mr, mc]) => {
        const sq = sqs[mr * 8 + mc];
        if (boardState[mr][mc]) sq.classList.add('capture-ring');
        else sq.classList.add('dot');
    });
}
function clearHL() {
    document.querySelectorAll('.square').forEach(s => s.classList.remove('selected','dot','capture-ring'));
}

function getMoves(piece, r, c, color) {
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
function addKing(m,r,c,col){
    // normal moves
    for(const[dr,dc]of[[-1,-1],[-1,0],[-1,1],[0,-1],[0,1],[1,-1],[1,0],[1,1]])
        if(inB(r+dr,c+dc)&&(!boardState[r+dr][c+dc]||boardState[r+dr][c+dc][0]!==col))
            m.push([r+dr,c+dc]);
    // castling hints (backend validates)
    if(col==='w'&&r===7&&c===4){
        if(!boardState[7][5]&&!boardState[7][6]) m.push([7,6]); // kingside
        if(!boardState[7][3]&&!boardState[7][2]&&!boardState[7][1]) m.push([7,2]); // queenside
    }
    if(col==='b'&&r===0&&c===4){
        if(!boardState[0][5]&&!boardState[0][6]) m.push([0,6]);
        if(!boardState[0][3]&&!boardState[0][2]&&!boardState[0][1]) m.push([0,2]);
    }
}
function inB(r,c){return r>=0&&r<8&&c>=0&&c<8;}

/* ================= MOVE ================= */
function doMove(fr, fc, tr, tc) {
    fetch(`/api/games/${gameId}/move`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': authHeader },
        body: JSON.stringify({ fromRow: fr, fromCol: fc, toRow: tr, toCol: tc })
    })
    .then(r => r.json())  // backend now returns JSON always
    .then(data => {
        const status  = data.status  || '';
        const message = data.message || '';

        if (status.startsWith('INVALID')) {
            // Show specific error message
            showToast(message, 'error');
            return;
        }

        loadBoard(); loadHistory(); loadTurn(); switchTimer();

        if (status === 'WHITE_WIN' || status === 'BLACK_WIN' || status === 'STALEMATE') {
            gameOver = true;
            clearInterval(timerInterval);
            showModal(status, message);
        }
    })
    .catch(err => { console.error(err); showToast('Move failed!', 'error'); });
}

/* ================= TOAST (replaces alert) ================= */
function showToast(msg, type) {
    let toast = document.getElementById('toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        document.body.appendChild(toast);
    }
    toast.innerText = msg;
    toast.className = 'toast ' + (type || '');
    toast.style.display = 'block';
    clearTimeout(toast._t);
    toast._t = setTimeout(() => { toast.style.display = 'none'; }, 3000);
}

/* ================= MODAL ================= */
function showModal(status, msg) {
    let icon, title;
    if      (status==='WHITE_WIN') { icon='♔'; title='White Wins!'; updateRating('white'); }
    else if (status==='BLACK_WIN') { icon='♚'; title='Black Wins!'; updateRating('black'); }
    else if (status==='STALEMATE') { icon='🤝'; title='Stalemate!'; updateRating('draw'); }
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
        el.innerText = ch >= 0 ? `Rating: +${ch} ⭐` : `Rating: ${ch} ⭐`;
        el.className = 'rating-change' + (ch < 0 ? ' negative' : '');
        document.getElementById('ratingDisplay').innerText = data.newRating;
    })
    .catch(() => { document.getElementById('ratingChange').innerText = ''; });
}

/* ================= HISTORY ================= */
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

/* ================= TURN ================= */
function loadTurn() {
    fetch(`/api/games/${gameId}/turn`, { headers: { 'Authorization': authHeader } })
    .then(r => r.json())
    .then(data => {
        // backend now returns {"turn": "white"} or {"turn": "black"}
        const turn = data.turn || data;
        activeTurn = typeof turn === 'string' ? turn : 'white';
        const cap = activeTurn.charAt(0).toUpperCase() + activeTurn.slice(1);

        // Turn banner above board
        const banner = document.getElementById('turnBanner');
        if (banner) {
            banner.innerText = '● ' + cap + "'s Turn";
            banner.className = 'turn-banner ' + (activeTurn === 'white' ? 'turn-white' : 'turn-black');
        }

        // Top bar turn
        document.getElementById('turn').innerText = 'Turn: ' + cap;

        document.getElementById('whiteTimerRow').className = 'timer-row'+(activeTurn==='white'?' active':'');
        document.getElementById('blackTimerRow').className = 'timer-row'+(activeTurn==='black'?' active':'');
        document.getElementById('whiteLabel').className = 'player-label'+(activeTurn==='white'?' active':'');
        document.getElementById('blackLabel').className = 'player-label'+(activeTurn==='black'?' active':'');
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
            if (whiteTime <= 0) { clearInterval(timerInterval); handleTimeout('white'); }
        } else {
            blackTime--;
            document.getElementById('blackTimer').innerText = fmt(blackTime);
            if (blackTime <= 30) document.getElementById('blackTimerRow').classList.add('low');
            if (blackTime <= 0) { clearInterval(timerInterval); handleTimeout('black'); }
        }
    }, 1000);
}

function handleTimeout(loser) {
    gameOver = true;
    const winner = loser === 'white' ? 'black' : 'white';
    fetch(`/api/games/${gameId}/timeout?loserColor=${loser}`, {
        method: 'POST', headers: { 'Authorization': authHeader }
    })
    .then(r => r.json())
    .then(data => {
        showModal(data.status || (winner==='white'?'WHITE_WIN':'BLACK_WIN'), data.message || '');
    });
}

function switchTimer() { activeTurn = activeTurn === 'white' ? 'black' : 'white'; }
function fmt(s){return`${Math.floor(s/60).toString().padStart(2,'0')}:${(s%60).toString().padStart(2,'0')}`;}

/* ================= RESET & UNDO ================= */
function resetGame() {
    clearInterval(timerInterval);
    fetch(`/api/games/${gameId}/reset`, { method:'POST', headers:{'Authorization':authHeader} })
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