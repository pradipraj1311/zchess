package com.zchess.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

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

    // ================= GAME CRUD =================

    // create game - logged in user automatically playerWhite bane
    @PostMapping
    public ResponseEntity<?> createGame(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(gameService.createGame(username));
    }

    // get ALL games - ADMIN only
    @GetMapping
    public ResponseEntity<?> getAllGames(Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access denied: Admins only");
        }
        return ResponseEntity.ok(gameService.getAllGames());
    }

    // get MY games - logged in user potani games joi shake
    @GetMapping("/my")
    public ResponseEntity<?> getMyGames(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(gameService.getGamesByUser(username));
    }

    // get game by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }

    // delete game - ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access denied: Admins only");
        }
        gameService.deleteGame(id);
        return ResponseEntity.ok("Game deleted");
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