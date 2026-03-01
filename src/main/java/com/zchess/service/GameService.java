package com.zchess.service;

import org.springframework.stereotype.Service;
import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import com.zchess.service.engine.ChessEngine;

@Service
public class GameService {

    private final GameRepository repository;
        private ChessEngine engine;

            public GameService(GameRepository repository) {
                    this.repository = repository;
                        }

                            public Game createGame() {

                                    engine = new ChessEngine();

                                            Game game = new Game();
                                                    game.setBoard(engine.getBoard());
                                                            game.setCurrentTurn(engine.getCurrentTurn());

                                                                    return repository.save(game);
                                                                        }
                                                                        }