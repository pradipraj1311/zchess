const boardDiv=document.getElementById("board")
const historyDiv=document.getElementById("history")
const turnLabel=document.getElementById("turn")

let selected=null
let board=[]
let turn="w"
let whiteTime=600
let blackTime=600
let timerInterval=null

function startTimer(){
    if(timerInterval) clearInterval(timerInterval)
    timerInterval = setInterval(()=>{
        if(turn==="w")
            whiteTime--
        else
            blackTime--
        updateClock()
    },1000)
}

function updateClock(){
    document.getElementById("timerWhite").innerText=
    "White: "+formatTime(whiteTime)
    document.getElementById("timerBlack").innerText=
    "Black: "+formatTime(blackTime)
}

function loadBoard(){
    fetch("/api/game/board")
    .then(r=>r.json())
    .then(data=>{
        board=data
        drawBoard()
    })
}

function drawBoard(){
boardDiv.innerHTML=""

for(let r=0;r<8;r++){

for(let c=0;c<8;c++){

const sq=document.createElement("div")

sq.className="square"

if((r+c)%2==0) sq.classList.add("light")
else sq.classList.add("dark")

sq.onclick=()=>squareClick(r,c)

const piece=board[r][c]

if(piece){

const img=document.createElement("img")

img.src="/pieces/"+piece+".svg"

img.className="piece"

sq.appendChild(img)

}

boardDiv.appendChild(sq)

}

}

updateTurn()

}

function squareClick(r,c){

if(selected==null){

selected={r:r,c:c}
return

}

movePiece(selected.r,selected.c,r,c)

selected=null

}

function movePiece(fr,fc,tr,tc){

fetch("/api/move",{

method:"POST",

headers:{"Content-Type":"application/json"},

body:JSON.stringify({

fromRow:fr,
fromCol:fc,
toRow:tr,
toCol:tc

})

})

.then(r=>r.json())

.then(data=>{

board=data

drawBoard()

toggleTurn()

loadHistory()

})

}

function toggleTurn(){

turn=(turn==="w")?"b":"w"

updateTurn()

}
function formatTime(t){

    let m=Math.floor(t/60)
    let s=t%60

    if(s<10) s="0"+s

    return m+":"+s

}

function updateTurn(){

if(turn==="w")
turnLabel.innerText="Turn: White"
else
turnLabel.innerText="Turn: Black"

}

function loadHistory(){

fetch("/api/game/history")

.then(r=>r.json())

.then(data=>{

historyDiv.innerHTML=""

data.forEach(m=>{

const li=document.createElement("li")
li.innerText=m

historyDiv.appendChild(li)

})

})

}

function resetGame(){

fetch("/api/game/reset",{method:"POST"})
.then(()=>location.reload())

}

function undoMove(){

fetch("/api/game/undo",{method:"POST"})
.then(()=>{

loadBoard()
loadHistory()

})

}

loadBoard()
loadHistory()
startTimer()