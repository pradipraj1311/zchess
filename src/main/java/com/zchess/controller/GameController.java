package com.zchess.controller;

import com.zchess.engine.Board;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @GetMapping("/api/game/board")
        public String[][] getBoard() {
                return Board.getBoard();
                    }
                    }