package com.zchess.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // CREATE GAME - reset board and set active game ID
    @PostMapping
    public ResponseEntity<?> createGame(Authentication auth) {
        moveService.reset(); // clear old state
        Game game = gameService.createGame(auth.getName());
        moveService.setActiveGame(game.getId()); // tell MoveService which game is active
        System.out.println("Game created: id=" + game.getId() + " user=" + auth.getName());
        return ResponseEntity.ok(game);
    }

    // ALL GAMES - admin only
    @GetMapping
    public ResponseEntity<?> getAllGames(Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("Access denied. Admins only.");
        return ResponseEntity.ok(gameService.getAllGames());
    }

    // MY GAMES - logged-in user's games
    @GetMapping("/my")
    public ResponseEntity<?> getMyGames(Authentication auth) {
        return ResponseEntity.ok(gameService.getGamesByUser(auth.getName()));
    }

    // GET GAME BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }

    // DELETE GAME - admin only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("Admins only");
        gameService.deleteGame(id);
        return ResponseEntity.ok("Game deleted");
    }

    // GET BOARD
    @GetMapping("/{id}/board")
    public ResponseEntity<?> board(@PathVariable Long id) {
        return ResponseEntity.ok(moveService.getBoard());
    }

    // MAKE MOVE - returns JSON with status and message
    @PostMapping("/{id}/move")
    public ResponseEntity<?> move(@PathVariable Long id, @RequestBody Move move) {
        String result = moveService.move(id, move);

        switch (result) {
            case "INVALID_NO_PIECE":
                return ResponseEntity.badRequest()
                        .body(Map.of("status","INVALID","message","No piece at selected square."));

            case "INVALID_WRONG_TURN":
                String turn = moveService.getTurn();
                return ResponseEntity.badRequest()
                        .body(Map.of("status","INVALID","message","It's "+turn+"'s turn, not yours!"));

            case "INVALID_OWN_PIECE":
                return ResponseEntity.badRequest()
                        .body(Map.of("status","INVALID","message","Cannot capture your own piece."));

            case "INVALID_ILLEGAL":
                return ResponseEntity.badRequest()
                        .body(Map.of("status","INVALID","message","Illegal move for this piece."));

            case "INVALID_KING_IN_CHECK":
                return ResponseEntity.badRequest()
                        .body(Map.of("status","INVALID","message","Your king is in check! Resolve the check first."));

            case "WHITE_WIN":
                return ResponseEntity.ok(Map.of("status","WHITE_WIN","message","Checkmate! White wins!"));

            case "BLACK_WIN":
                return ResponseEntity.ok(Map.of("status","BLACK_WIN","message","Checkmate! Black wins!"));

            case "STALEMATE":
                return ResponseEntity.ok(Map.of("status","STALEMATE","message","Stalemate! Game is a draw."));

            default: // OK
                return ResponseEntity.ok(Map.of("status","OK","message","Move accepted."));
        }
    }

    // GET RESULT
    @GetMapping("/{id}/result")
    public ResponseEntity<?> result(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("result", moveService.getGameResult()));
    }

    // TIMEOUT
    @PostMapping("/{id}/timeout")
    public ResponseEntity<?> timeout(@PathVariable Long id, @RequestParam String loserColor) {
        String result = moveService.declareTimeout(id, loserColor);
        String winner = loserColor.equals("white") ? "Black" : "White";
        return ResponseEntity.ok(Map.of(
                "status",  result,
                "message", loserColor + " ran out of time! " + winner + " wins!"
        ));
    }

    // HISTORY
    @GetMapping("/{id}/history/white")
    public List<String> whiteHistory(@PathVariable Long id) { return moveService.getWhiteHistory(); }

    @GetMapping("/{id}/history/black")
    public List<String> blackHistory(@PathVariable Long id) { return moveService.getBlackHistory(); }

    // TURN - returns JSON
    @GetMapping("/{id}/turn")
    public ResponseEntity<?> turn(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("turn", moveService.getTurn()));
    }

    // RESET
    @PostMapping("/{id}/reset")
    public ResponseEntity<?> reset(@PathVariable Long id) {
        moveService.reset();
        return ResponseEntity.ok(Map.of("message","Game reset successfully"));
    }

    // UNDO
    @PostMapping("/{id}/undo")
    public ResponseEntity<?> undo(@PathVariable Long id) {
        moveService.undo();
        return ResponseEntity.ok(Map.of("message","Last move undone"));
    }
}