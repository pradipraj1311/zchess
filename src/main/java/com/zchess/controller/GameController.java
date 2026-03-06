package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
        private GameService gameService;

            @PostMapping
                public Game createGame(@RequestBody Game game) {
                        return gameService.createGame(game);
                            }

                                @GetMapping
                                    public List<Game> getAllGames() {
                                            return gameService.getAllGames();
                                                }

                                                    @GetMapping("/{id}")
                                                        public Game getGame(@PathVariable Long id) {
                                                                return gameService.getGame(id);
                                                                    }

                                                                        @DeleteMapping("/{id}")
                                                                            public void deleteGame(@PathVariable Long id) {
                                                                                    gameService.deleteGame(id);
                                                                                        }
                                                                                        }