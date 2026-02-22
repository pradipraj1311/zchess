package com.zchess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;

@Service
public class GameService {

    @Autowired
        private GameRepository gameRepository;

            // CREATE NEW GAME
                public Game createGame() {

                        Game g = new Game();

                                // initial chess board
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

                                                                                                                                                                                                return gameRepository.save(g);
                                                                                                                                                                                                    }

                                                                                                                                                                                                        // GET GAME BY ID
                                                                                                                                                                                                            public Game getGame(Long id) {
                                                                                                                                                                                                                    return gameRepository.findById(id).orElse(null);
                                                                                                                                                                                                                        }

                                                                                                                                                                                                                            // SAVE GAME AFTER MOVE
                                                                                                                                                                                                                                public Game save(Game g) {
                                                                                                                                                                                                                                        return gameRepository.save(g);
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            }
