package com.zchess.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zchess.entity.Game;
import com.zchess.entity.Move;
import com.zchess.service.GameService;
import com.zchess.service.MoveService;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final MoveService moveService;
    private final GameService gameService;

    public GameController(MoveService moveService, GameService gameService) {
        this.moveService = moveService;
        this.gameService = gameService;
    }

    //  CREATE GAME
    @PostMapping
    public Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    //  GET ALL GAMES
    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    //  GET GAME BY ID
    @GetMapping("/{id}")
    public Game getGame(@PathVariable Long id) {
        return gameService.getGame(id);
    }

    //  DELETE GAME
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

    // ================== GAME PLAY ==================

    // 🔹 BOARD
    @GetMapping("/{id}/board")
    public String[][] board(@PathVariable Long id) {
        return moveService.getBoard();
    }

    // 🔹 MAKE MOVE
    @PostMapping("/{id}/move")
    public String[][] move(@PathVariable Long id, @RequestBody Move move) {
    	return moveService.move(id, move);
    	}

    // 🔹 WHITE HISTORY
    @GetMapping("/{id}/history/white")
    public List<String> getWhiteHistory(@PathVariable Long id) {
        return moveService.getWhiteHistory();
    }

    // 🔹 BLACK HISTORY
    @GetMapping("/{id}/history/black")
    public List<String> getBlackHistory(@PathVariable Long id) {
        return moveService.getBlackHistory();
    }

    // TURN
    @GetMapping("/{id}/turn")
    public String turn(@PathVariable Long id) {
        return moveService.getTurn();
    }

    //  RESET
    @PostMapping("/{id}/reset")
    public void reset(@PathVariable Long id) {
        moveService.reset();
    }

    //  UNDO
    @PostMapping("/{id}/undo")
    public void undo(@PathVariable Long id) {
        moveService.undo();
    }
}