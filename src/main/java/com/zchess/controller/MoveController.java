package com.zchess.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.zchess.entity.Move;
import com.zchess.repository.MoveRepository;

@RestController
@RequestMapping("/api/moves")
public class MoveController {

    private final MoveRepository moveRepository;

    public MoveController(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    // GET ALL MOVES - admin only
    @GetMapping("/all")
    public ResponseEntity<?> getAllMoves(Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("Admins only");
        List<Move> moves = moveRepository.findAll();
        return ResponseEntity.ok(moves);
    }

    // GET MOVES BY GAME ID - admin only
    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getMovesByGame(@PathVariable Long gameId, Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("Admins only");
        List<Move> moves = moveRepository.findByGameId(gameId);
        return ResponseEntity.ok(moves);
    }
}