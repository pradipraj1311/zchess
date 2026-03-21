package com.zchess.controller;

import com.zchess.service.MoveService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final MoveService moveService;

    public GameController(MoveService moveService) {
        this.moveService = moveService;
    }

    // Board
    @GetMapping("/board")
    public String[][] board() {
        return moveService.getBoard();
    }

    // White history
    @GetMapping("/history/white")
    public List<String> getWhiteHistory() {
        return moveService.getWhiteHistory();
    }

    //Black history
    @GetMapping("/history/black")
    public List<String> getBlackHistory() {
        return moveService.getBlackHistory();
    }

    // Turn
    @GetMapping("/turn")
    public String turn() {
        return moveService.getTurn();
    }

    // Reset
    @PostMapping("/reset")
    public void reset() {
        moveService.reset();
    }

    //  Undo
    @PostMapping("/undo")
    public void undo() {
        moveService.undo();
    }
}