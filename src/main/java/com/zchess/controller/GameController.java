package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import com.zchess.service.ChessService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin
public class GameController {

    private final GameRepository repo;
        private final ChessService chess;

            public GameController(GameRepository repo, ChessService chess){
                    this.repo = repo;
                            this.chess = chess;
                                }

                                    @PostMapping("/create")
                                        public Game create(){
                                                Game g = new Game();
                                                        g.setBoardState(chess.initialBoard());
                                                                g.setCurrentTurn("white");
                                                                        g.setMoveHistory(g.getBoardState());
                                                                                return repo.save(g);
                                                                                    }

                                                                                        @GetMapping("/{id}")
                                                                                            public Game get(@PathVariable Long id){
                                                                                                    return repo.findById(id).orElseThrow();
                                                                                                        }
                                                                                                        }