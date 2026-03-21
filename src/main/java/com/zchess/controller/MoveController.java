package com.zchess.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zchess.entity.Move;
import com.zchess.service.MoveService;

@RestController
@RequestMapping("/api")
public class MoveController {

    private final MoveService moveService;

        public MoveController(MoveService moveService){
                this.moveService = moveService;
                    }

                        @PostMapping("/move")
                            public String[][] move(@RequestBody Move move){

                                    return moveService.move(move);

                                        }
                                        }