package com.zchess.controller;

import com.zchess.entity.Move;
import com.zchess.service.MoveService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moves")
public class MoveController {

    private final MoveService moveService;

        public MoveController(MoveService moveService){
                this.moveService = moveService;
                    }

                        @PostMapping
                            public Move addMove(@RequestBody Move move){
                                    return moveService.saveMove(move);
                                        }

                                            @GetMapping("/game/{gameId}")
                                                public List<Move> getMoves(@PathVariable Long gameId){
                                                        return moveService.getMoves(gameId);
                                                            }
                                                            }