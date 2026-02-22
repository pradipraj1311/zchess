package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
        GameRepository gameRepo;

            // CREATE GAME
                @PostMapping("/create")
                    public Game createGame() {

                            Game g = new Game();

                                    String start =
                                                    "br,bn,bb,bq,bk,bb,bn,br," +
                                                                    "bp,bp,bp,bp,bp,bp,bp,bp," +
                                                                                    ".,.,.,.,.,.,.,.," +
                                                                                                    ".,.,.,.,.,.,.,.," +
                                                                                                                    ".,.,.,.,.,.,.,.," +
                                                                                                                                    ".,.,.,.,.,.,.,.," +
                                                                                                                                                    "wp,wp,wp,wp,wp,wp,wp,wp," +
                                                                                                                                                                    "wr,wn,wb,wq,wk,wb,wn,wr";

                                                                                                                                                                            g.setBoardState(start);
                                                                                                                                                                                    g.setCurrentTurn("white");

                                                                                                                                                                                            gameRepo.save(g);
                                                                                                                                                                                                    return g;
                                                                                                                                                                                                        }

                                                                                                                                                                                                            // GET GAME
                                                                                                                                                                                                                @GetMapping("/{id}")
                                                                                                                                                                                                                    public Game getGame(@PathVariable Long id) {
                                                                                                                                                                                                                            return gameRepo.findById(id).orElseThrow();
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                }
                                                