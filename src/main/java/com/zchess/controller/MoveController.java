package com.zchess.controller;

import org.springframework.web.bind.annotation.*;
import com.zchess.repository.*;
import com.zchess.entity.*;

import java.util.List;

@RestController
@RequestMapping("/api/moves")
public class MoveController {

    private final MoveRepository moveRepository;
        private final GameRepository gameRepository;

            public MoveController(MoveRepository moveRepository, GameRepository gameRepository){
                    this.moveRepository = moveRepository;
                            this.gameRepository = gameRepository;
                                }

                                    @GetMapping("/add/{gameId}/{notation}")
                                        public Move addMove(@PathVariable Long gameId, @PathVariable String notation){

                                                Game game = gameRepository.findById(gameId).orElseThrow();

                                                        List<Move> moves = moveRepository.findAll()
                                                                        .stream()
                                                                                        .filter(m -> m.getGame().getId().equals(gameId))
                                                                                                        .toList();

                                                                                                                int moveNumber = moves.size() + 1;

                                                                                                                        String color = (moveNumber % 2 == 1) ? "WHITE" : "BLACK";

                                                                                                                                Move move = new Move();
                                                                                                                                        move.setGame(game);
                                                                                                                                                move.setMoveNumber(moveNumber);
                                                                                                                                                        move.setPlayerColor(color);
                                                                                                                                                                move.setMoveNotation(notation);

                                                                                                                                                                        return moveRepository.save(move);
                                                                                                                                                                            }

                                                                                                                                                                                @GetMapping("/game/{gameId}")
                                                                                                                                                                                    public List<Move> getMoves(@PathVariable Long gameId){
                                                                                                                                                                                            return moveRepository.findAll()
                                                                                                                                                                                                            .stream()
                                                                                                                                                                                                                            .filter(m -> m.getGame().getId().equals(gameId))
                                                                                                                                                                                                                                            .toList();
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                }