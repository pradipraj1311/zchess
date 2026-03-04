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
                            public Move addMove(
                                        @RequestParam Long gameId,
                                                    @RequestParam String from,
                                                                @RequestParam String to,
                                                                            @RequestParam int moveNumber){

                                                                                    return moveService.addMove(gameId,from,to,moveNumber);
                                                                                        }

                                                                                            @GetMapping("/game/{gameId}")
                                                                                                public List<Move> getMoves(@PathVariable Long gameId){
                                                                                                        return moveService.getMovesByGame(gameId);
                                                                                                            }
                                                                                                            }