package com.zchess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zchess.service.MoveService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final MoveService moveService;

        public GameController(MoveService moveService) {
                this.moveService = moveService;
                    }

                        @GetMapping("/board")
                            public String[][] board() {
                                    return moveService.getBoard();
                                        }

                                            @GetMapping("/history")
                                                public Object history() {
                                                        return moveService.getHistory();
                                                            }

                                                                @GetMapping("/turn")
                                                                    public String turn() {
                                                                            return moveService.getTurn();
                                                                                }

                                                                                    @PostMapping("/reset")
                                                                                        public void reset() {
                                                                                                moveService.reset();
                                                                                                    }

                                                                                                        @PostMapping("/undo")
                                                                                                            public void undo() {
                                                                                                                    moveService.undo();
                                                                                                                        }
                                                                                                                        }