const boardDiv = document.getElementById("board")

let selected = null
let board = []

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

})
.catch(err=>console.error(err))

}

loadBoard()