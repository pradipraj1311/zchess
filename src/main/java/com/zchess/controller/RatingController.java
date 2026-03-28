package com.zchess.controller;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.zchess.entity.Rating;
import com.zchess.service.RatingService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // leaderboard
    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    // potani rating
    @GetMapping("/me")
    public ResponseEntity<?> getMyRating(Authentication auth) {
        return ResponseEntity.ok(ratingService.getRatingByUsername(auth.getName()));
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateRating(
            @RequestParam String whitePlayer,
            @RequestParam String blackPlayer,
            @RequestParam String result) {

        double score;
        switch (result.toLowerCase()) {
            case "white": score = 1.0; break;
            case "black": score = 0.0; break;
            case "draw":  score = 0.5; break;
            default: return ResponseEntity.badRequest().body("Invalid result. Use: white, black, draw");
        }

        ratingService.updateRatings(whitePlayer, blackPlayer, score);
        return ResponseEntity.ok("Ratings updated successfully");
    }
    // solo update - white player vs local black (no account)
    // result: "white" | "black" | "draw"
    @PostMapping("/update-solo")
    public ResponseEntity<?> updateSoloRating(
            @RequestParam String username,
            @RequestParam String result) {
 
        RatingService.SoloRatingResult res = ratingService.updateSoloRating(username, result);
        return ResponseEntity.ok(res);
    }
    
    
}