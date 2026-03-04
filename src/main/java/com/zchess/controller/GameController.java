package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.service.GameService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

        public GameController(GameService gameService){
                this.gameService = gameService;
                    }

                        @PostMapping
                            public Game createGame(@RequestParam Long userId){
                                    return gameService.createGame(userId);
                                        }

                                            @GetMapping
                                                public List<Game> getAllGames(){
                                                        return gameService.getAllGames();
                                                            }

                                                                @GetMapping("/user/{userId}")
                                                                    public List<Game> getUserGames(@PathVariable Long userId){
                                                                            return gameService.getGamesByUser(userId);
                                                                                }

                                                                                    @DeleteMapping("/{id}")
                                                                                        public void deleteGame(@PathVariable Long id){
                                                                                                gameService.deleteGame(id);
                                                                                                    }
                                                                                                    }