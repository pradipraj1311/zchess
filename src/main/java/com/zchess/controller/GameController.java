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

    // CREATE GAME
    @PostMapping
    public ResponseEntity<?> createGame(Authentication auth) {
        // reset board state for new game / new user
        moveService.reset();
        return ResponseEntity.ok(gameService.createGame(auth.getName()));
    }

    // ALL GAMES - admin only
    @GetMapping
    public ResponseEntity<?> getAllGames(Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("Admins only");
        return ResponseEntity.ok(gameService.getAllGames());
    }

    // MY GAMES
    @GetMapping("/my")
    public ResponseEntity<?> getMyGames(Authentication auth) {
        return ResponseEntity.ok(gameService.getGamesByUser(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }

    // DELETE - admin only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("Admins only");
        gameService.deleteGame(id);
        return ResponseEntity.ok("Game deleted");
    }

    // BOARD
    @GetMapping("/{id}/board")
    public String[][] board(@PathVariable Long id) {
        return moveService.getBoard();
    }

    // MOVE - returns OK | WHITE_WIN | BLACK_WIN | STALEMATE | INVALID(400)
    @PostMapping("/{id}/move")
    public ResponseEntity<?> move(@PathVariable Long id, @RequestBody Move move) {
        String result = moveService.move(id, move);
        if (result.equals("INVALID")) {
            return ResponseEntity.badRequest().body("INVALID");
        }
        return ResponseEntity.ok(result);
    }

    // GAME RESULT
    @GetMapping("/{id}/result")
    public String result(@PathVariable Long id) {
        return moveService.getGameResult();
    }

    // TIMEOUT
    @PostMapping("/{id}/timeout")
    public ResponseEntity<?> timeout(@PathVariable Long id,
                                     @RequestParam String loserColor) {
        String result = moveService.declareTimeout(id, loserColor);
        return ResponseEntity.ok(result);
    }

    // HISTORY
    @GetMapping("/{id}/history/white")
    public List<String> getWhiteHistory(@PathVariable Long id) {
        return moveService.getWhiteHistory();
    }

    @GetMapping("/{id}/history/black")
    public List<String> getBlackHistory(@PathVariable Long id) {
        return moveService.getBlackHistory();
    }

    // TURN
    @GetMapping("/{id}/turn")
    public String turn(@PathVariable Long id) {
        return moveService.getTurn();
    }

    // RESET
    @PostMapping("/{id}/reset")
    public void reset(@PathVariable Long id) {
        moveService.reset();
    }

    // UNDO
    @PostMapping("/{id}/undo")
    public void undo(@PathVariable Long id) {
        moveService.undo();
    }
}