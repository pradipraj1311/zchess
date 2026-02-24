package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class GameController {

    private final GameService service;

        public GameController(GameService service) {
                this.service = service;
                    }

                        @PostMapping("/create")
                            public Game create() {
                                    return service.createGame();
                                        }

                                            @PostMapping("/move/{id}")
                                                public Game move(@PathVariable Long id,
                                                                     @RequestParam String from,
                                                                                          @RequestParam String to) {

                                                                                                  return service.makeMove(id, from, to);
                                                                                                      }

                                                                                                          @GetMapping("/game/{id}")
                                                                                                              public Game get(@PathVariable Long id) {
                                                                                                                      return service.get(id);
                                                                                                                          }
                                                                                                                          }