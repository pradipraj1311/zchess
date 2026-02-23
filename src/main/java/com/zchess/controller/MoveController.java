package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import com.zchess.service.ChessService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/move")
public class MoveController {

    private final GameRepository gameRepo;
        private final ChessService chess;

            public MoveController(GameRepository g,ChessService c){
                    this.gameRepo=g;
                            this.chess=c;
                                }

                                    @PostMapping("/{id}")
                                        public Game move(@PathVariable Long id,
                                                             @RequestParam int from,
                                                                                  @RequestParam int to){

                                                                                          Game g=gameRepo.findById(id).orElseThrow();

                                                                                                  String[] board=g.getBoardState().split(",");
                                                                                                          String turn=g.getCurrentTurn();

                                                                                                                  if(!chess.tryMove(board,from,to,turn))
                                                                                                                              return g;

                                                                                                                                      board[to]=board[from];
                                                                                                                                              board[from]=".";

                                                                                                                                                      g.setBoardState(String.join(",",board));

                                                                                                                                                              String next=turn.equals("white")?"black":"white";
                                                                                                                                                                      g.setCurrentTurn(next);

                                                                                                                                                                              if(chess.isCheckmate(board,next))
                                                                                                                                                                                          System.out.println("CHECKMATE!");

                                                                                                                                                                                                  gameRepo.save(g);
                                                                                                                                                                                                          return g;
                                                                                                                                                                                                              }
                                                                                                                                                                                                              }