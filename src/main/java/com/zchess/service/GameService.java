package com.zchess.service;

import com.zchess.entity.*;
import com.zchess.repository.GameRepository;
import com.zchess.service.engine.ChessEngine;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository repo;
        private final ChessEngine engine = new ChessEngine();

            public GameService(GameRepository repo) {
                    this.repo = repo;
                        }

                            public Game create() {

                                    Game game = new Game();

                                            // Initialize pieces properly (full board)

                                                    repo.save(game);
                                                            return game;
                                                                }

                                                                    public Game move(Long id, int from, int to) {

                                                                            Game game = repo.findById(id)
                                                                                            .orElseThrow();

                                                                                                    engine.executeMove(game, from, to);

                                                                                                            return repo.save(game);
                                                                                                                }

                                                                                                                    public Game get(Long id) {
                                                                                                                            return repo.findById(id).orElseThrow();
                                                                                                                                }
                                                                                                                                }