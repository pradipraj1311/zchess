package com.zchess.controller;

import com.zchess.entity.Move;
import com.zchess.service.MoveService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MoveController {

    private final MoveService moveService;

        public MoveController(MoveService moveService){
                this.moveService = moveService;
                    }

                        @PostMapping("/move")
                            public String[][] move(@RequestBody Move move){

                                    return moveService.saveMove(move);

                                        }
                                        }