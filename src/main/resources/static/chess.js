const boardDiv = document.getElementById("board")
const historyDiv = document.getElementById("history")
const turnLabel = document.getElementById("turn")

let selected = null
let board = []
let turn = "w"


function loadBoard(){

fetch("/api/game/board")

.then(res=>res.json())

.then(data=>{

board=data

drawBoard()

})

}


function drawBoard(){

boardDiv.innerHTML=""

for(let r=0;r<8;r++){

for(let c=0;c<8;c++){

const square=document.createElement("div")

square.className="square"

if((r+c)%2==0)
square.classList.add("light")
else
square.classList.add("dark")

square.onclick=()=>squareClick(r,c)

const piece=board[r][c]

if(piece){

const img=document.createElement("img")

img.src="/pieces/"+piece+".svg"

img.className="piece"

square.appendChild(img)

}

boardDiv.appendChild(square)

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

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({

fromRow:fr,
fromCol:fc,
toRow:tr,
toCol:tc

})

})

.then(res=>res.json())

.then(data=>{

board=data

drawBoard()

loadHistory()

toggleTurn()

})

}


function toggleTurn(){

if(turn==="w")
turn="b"
else
turn="w"

updateTurn()

}


function updateTurn(){

if(turn==="w")
turnLabel.innerText="Turn: White"
else
turnLabel.innerText="Turn: Black"

}


function loadHistory(){

fetch("/api/game/history")

.then(res=>res.json())

.then(data=>{

historyDiv.innerHTML=""

data.forEach(move=>{

const li=document.createElement("li")

li.innerText=move

historyDiv.appendChild(li)

})

})

}


function resetGame(){

fetch("/api/game/reset",{
method:"POST"
})

.then(()=>{

loadBoard()

loadHistory()

turn="w"

updateTurn()

})

}


function undoMove(){

fetch("/api/game/undo",{
method:"POST"
})

.then(()=>{

loadBoard()

loadHistory()

})

}


loadBoard()
loadHistory()