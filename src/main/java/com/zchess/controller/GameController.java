package com.zchess.controller;

import com.zchess.engine.Board;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameController {

    @GetMapping("/game/board")
        public String[][] board(){

                return Board.getBoard();

                    }

                    }