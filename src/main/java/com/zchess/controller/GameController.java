package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService service;

        public GameController(GameService service) {
                this.service = service;
                    }

                        @GetMapping("/create")
                            public Game create() {
                                    return service.create();
                                        }

                                            @PostMapping("/move/{id}")
                                                public Game move(@PathVariable Long id,
                                                                     @RequestParam int from,
                                                                                          @RequestParam int to) {

                                                                                                  return service.move(id, from, to);
                                                                                                      }

                                                                                                          @GetMapping("/{id}")
                                                                                                              public Game get(@PathVariable Long id) {
                                                                                                                      return service.get(id);
                                                                                                                          }
                                                                                                                          }