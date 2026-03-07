package com.zchess.service;
import com.zchess.engine.Board;
import com.zchess.engine.MoveValidator;
import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
        private GameRepository gameRepository;

            public Game createGame(Game game) {
                    return gameRepository.save(game);
                        }

                            public List<Game> getAllGames() {
                                    return gameRepository.findAll();
                                        }

                                            public Game getGame(Long id) {
                                                    return gameRepository.findById(id)
                                                                    .orElseThrow(() -> new RuntimeException("Game not found"));
                                                                        }

                                                                            public void deleteGame(Long id) {
                                                                                    gameRepository.deleteById(id);
                                                                                        }
                                                                                        }