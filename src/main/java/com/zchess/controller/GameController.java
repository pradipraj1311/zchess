package com.zchess.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // create a new game
    @PostMapping
    public Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    // get all games
    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    // get game by id
    @GetMapping("/{id}")
    public Game getGame(@PathVariable Long id) {
        return gameService.getGame(id);
    }

    // delete game
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

    // ================= GAME PLAY =================

    // get board state
    @GetMapping("/{id}/board")
    public String[][] board(@PathVariable Long id) {
        return moveService.getBoard();
    }

    // make a move - 200 if valid, 400 if invalid
    @PostMapping("/{id}/move")
    public ResponseEntity<?> move(@PathVariable Long id, @RequestBody Move move) {
        boolean success = moveService.move(id, move);
        if (!success) {
            return ResponseEntity.badRequest().body("Invalid move");
        }
        return ResponseEntity.ok(moveService.getBoard());
    }

    // get white move history
    @GetMapping("/{id}/history/white")
    public List<String> getWhiteHistory(@PathVariable Long id) {
        return moveService.getWhiteHistory();
    }

    // get black move history
    @GetMapping("/{id}/history/black")
    public List<String> getBlackHistory(@PathVariable Long id) {
        return moveService.getBlackHistory();
    }

    // get current turn
    @GetMapping("/{id}/turn")
    public String turn(@PathVariable Long id) {
        return moveService.getTurn();
    }

    // reset game
    @PostMapping("/{id}/reset")
    public void reset(@PathVariable Long id) {
        moveService.reset();
    }

    // undo last move
    @PostMapping("/{id}/undo")
    public void undo(@PathVariable Long id) {
        moveService.undo();
    }
}