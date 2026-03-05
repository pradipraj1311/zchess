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
                            public Game createGame(){
                                    return gameService.createGame();
                                        }

                                            @GetMapping
                                                public List<Game> getGames(){
                                                        return gameService.getAllGames();
                                                            }

                                                                @DeleteMapping("/{id}")
                                                                    public void deleteGame(@PathVariable Long id){
                                                                            gameService.deleteGame(id);
                                                                                }
                                                                                }