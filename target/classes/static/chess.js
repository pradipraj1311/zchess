const boardDiv = document.getElementById("board")

let selected = null
let board = []

function loadBoard(){

 fetch("/api/game/board")
  .then(r => r.json())
   .then(data => {
       board = data
           drawBoard()
            })
            }

            function drawBoard(){

             boardDiv.innerHTML = ""

              for(let r=0;r<8;r++){

                 for(let c=0;c<8;c++){

                       const sq = document.createElement("div")

                             sq.classList.add("square")
                                   sq.classList.add((r+c)%2==0 ? "white":"black")

                                         sq.dataset.row = r
                                               sq.dataset.col = c

                                                     sq.onclick = squareClick

                                                           const piece = board[r][c]

                                                                 if(piece !== ""){

                                                                          const img = document.createElement("img")
                                                                                   img.src = "/pieces/" + piece + ".svg"
                                                                                            img.className = "piece"

                                                                                                     sq.appendChild(img)
                                                                                                           }

                                                                                                                 boardDiv.appendChild(sq)
                                                                                                                    }
                                                                                                                     }
                                                                                                                     }

                                                                                                                     function squareClick(){

                                                                                                                      const r = this.dataset.row
                                                                                                                       const c = this.dataset.col

                                                                                                                        if(selected == null){

                                                                                                                            selected = {r,c}
                                                                                                                                return
                                                                                                                                 }

                                                                                                                                  const move = {
                                                                                                                                     fromRow: parseInt(selected.r),
                                                                                                                                        fromCol: parseInt(selected.c),
                                                                                                                                           toRow: parseInt(r),
                                                                                                                                              toCol: parseInt(c)
                                                                                                                                               }

                                                                                                                                                fetch("/api/move",{
                                                                                                                                                   method:"POST",
                                                                                                                                                      headers:{
                                                                                                                                                            "Content-Type":"application/json"
                                                                                                                                                               },
                                                                                                                                                                  body:JSON.stringify(move)
                                                                                                                                                                   })
                                                                                                                                                                    .then(r => r.json())
                                                                                                                                                                     .then(newBoard=>{
                                                                                                                                                                         board = newBoard
                                                                                                                                                                             drawBoard()
                                                                                                                                                                              })

                                                                                                                                                                               selected = null
                                                                                                                                                                               }

                                                                                                                                                                               loadBoard()