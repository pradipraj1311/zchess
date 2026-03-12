package com.zchess.controller;

import com.zchess.engine.Board;
import com.zchess.engine.GameState;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @GetMapping("/board")
        public String[][] getBoard(){
                return Board.getBoard();
                    }

                        @GetMapping("/history")
                            public List<String> history(){
                                    return GameState.getHistory();
                                        }
                                        @PostMapping("/undo")
                                        public String undo(){

                                            GameState.undo();

                                                return "Undo move";
                                                }

                                            @PostMapping("/reset")
                                                public String reset(){
                                                    Board.resetBoard();
                                                        GameState.reset();
                                                                return "Game reset";
                                                                    }

                                                                    }