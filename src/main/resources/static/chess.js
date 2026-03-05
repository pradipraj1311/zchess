const board = document.getElementById("board")

let selected = null

const initialBoard = [
["br","bn","bb","bq","bk","bb","bn","br"],
["bp","bp","bp","bp","bp","bp","bp","bp"],
["","","","","","","",""],
["","","","","","","",""],
["","","","","","","",""],
["","","","","","","",""],
["wp","wp","wp","wp","wp","wp","wp","wp"],
["wr","wn","wb","wq","wk","wb","wn","wr"]
]

function drawBoard(){

board.innerHTML=""

for(let r=0;r<8;r++){

for(let c=0;c<8;c++){

const sq=document.createElement("div")

sq.classList.add("square")
sq.classList.add((r+c)%2==0?"white":"black")

sq.dataset.row=r
sq.dataset.col=c

sq.onclick=squareClick

const piece=initialBoard[r][c]

if(piece!=""){
const img=document.createElement("img")
img.src="pieces/"+piece+".png"
sq.appendChild(img)
}

board.appendChild(sq)
}
}
}

function squareClick(){

const r=this.dataset.row
const c=this.dataset.col

if(selected==null){
selected={r,c}
}
else{

movePiece(selected.r,selected.c,r,c)

selected=null
}
}

function movePiece(fr,fc,tr,tc){
    fetch("/api/moves",{
        method:"POST",
        headers:{
        "Content-Type":"application/json"
        },
        body:JSON.stringify({
        fromPosition:fr+","+fc,
        toPosition:tr+","+tc,
        moveNumber:1
        })
        })

initialBoard[tr][tc]=initialBoard[fr][fc]
initialBoard[fr][fc]=""

drawBoard()

}

drawBoard()