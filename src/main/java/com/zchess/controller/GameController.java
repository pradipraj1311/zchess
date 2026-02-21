package com.zchess.controller;

import org.springframework.web.bind.annotation.*;
import com.zchess.repository.*;
import com.zchess.entity.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository gameRepository;
        private final UserRepository userRepository;

            public GameController(GameRepository gameRepository, UserRepository userRepository) {
                    this.gameRepository = gameRepository;
                            this.userRepository = userRepository;
                                }

                                    @GetMapping("/create/{userId}")
                                        public Game createGame(@PathVariable Long userId) {
                                                User user = userRepository.findById(userId).orElseThrow();
                                                        Game game = new Game();
                                                                game.setUser(user);
                                                                        game.setStatus("IN_PROGRESS");
                                                                                game.setResult("NONE");
                                                                                        return gameRepository.save(game);
                                                                                            }

                                                                                                @GetMapping("/{id}")
                                                                                                    public Game getGame(@PathVariable Long id){
                                                                                                            return gameRepository.findById(id).orElse(null);
                                                                                                                }
                                                                                                                }