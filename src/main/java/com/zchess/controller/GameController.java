package com.zchess.controller;

import org.springframework.web.bind.annotation.*;
import com.zchess.entity.Game;
import com.zchess.service.engine.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin
public class GameController {

    private ChessEngine engine;

        @PostMapping("/create")
            public Game createGame() {

                    engine = new ChessEngine();

                            Game game = new Game();
                                    game.setBoard(engine.getBoard());
                                            game.setCurrentTurn(engine.getCurrentTurn());

                                                    return game;
                                                        }

                                                            @PostMapping("/move")
                                                                public Game move(
                                                                            @RequestParam int fromRow,
                                                                                        @RequestParam int fromCol,
                                                                                                    @RequestParam int toRow,
                                                                                                                @RequestParam int toCol) {

                                                                                                                        if(engine == null)
                                                                                                                                    return null;

                                                                                                                                            Move move = new Move(
                                                                                                                                                            new Position(fromRow, fromCol),
                                                                                                                                                                            new Position(toRow, toCol)
                                                                                                                                                                                    );

                                                                                                                                                                                            engine.makeMove(move);

                                                                                                                                                                                                    Game game = new Game();
                                                                                                                                                                                                            game.setBoard(engine.getBoard());
                                                                                                                                                                                                                    game.setCurrentTurn(engine.getCurrentTurn());

                                                                                                                                                                                                                            return game;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                }