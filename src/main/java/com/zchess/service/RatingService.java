package com.zchess.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zchess.entity.Rating;
import com.zchess.entity.User;
import com.zchess.repository.RatingRepository;
import com.zchess.repository.UserRepository;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository   userRepository;

    private static final int K       = 32;
    private static final int BASE    = 1500; // assumed opponent rating (local player)
    private static final int MIN_RATING = 100;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository   = userRepository;
    }

    public Rating createRating(User user) {
        Rating r = new Rating(user);
        return ratingRepository.save(r);
    }

    public Rating getRating(User user) {
        return ratingRepository.findByUser(user).orElseGet(() -> createRating(user));
    }

    public Rating getRatingByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getRating(user);
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAllByOrderByRatingDesc();
    }

    // ================= ELO - TWO PLAYERS =================
    // result: 1.0=white win, 0.0=black win, 0.5=draw
    public void updateRatings(String whiteUsername, String blackUsername, double result) {
        User wUser = userRepository.findByUsername(whiteUsername)
                .orElseThrow(() -> new RuntimeException("White not found"));
        User bUser = userRepository.findByUsername(blackUsername)
                .orElseThrow(() -> new RuntimeException("Black not found"));

        Rating wRating = getRating(wUser);
        Rating bRating = getRating(bUser);

        int rA = wRating.getRating(), rB = bRating.getRating();
        double eA = 1.0 / (1.0 + Math.pow(10, (rB - rA) / 400.0));
        double eB = 1.0 / (1.0 + Math.pow(10, (rA - rB) / 400.0));

        int newA = Math.max(MIN_RATING, (int) Math.round(rA + K * (result - eA)));
        int newB = Math.max(MIN_RATING, (int) Math.round(rB + K * ((1 - result) - eB)));

        // white
        wRating.setRating(newA);
        wRating.setGamesPlayed(wRating.getGamesPlayed() + 1);
        if (result == 1.0)      wRating.setWins(wRating.getWins() + 1);
        else if (result == 0.0) wRating.setLosses(wRating.getLosses() + 1);
        else                    wRating.setDraws(wRating.getDraws() + 1);

        // black
        bRating.setRating(newB);
        bRating.setGamesPlayed(bRating.getGamesPlayed() + 1);
        if (result == 0.0)      bRating.setWins(bRating.getWins() + 1);
        else if (result == 1.0) bRating.setLosses(bRating.getLosses() + 1);
        else                    bRating.setDraws(bRating.getDraws() + 1);

        ratingRepository.save(wRating);
        ratingRepository.save(bRating);
    }

    // ================= ELO - SOLO (vs local black player) =================
    // Black has no account - white player rated against BASE opponent (1500)
    // result: "white" | "black" | "draw"
    public SoloRatingResult updateSoloRating(String username, String result) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Rating rating = getRating(user);

        int oldRating = rating.getRating();
        double actual;

        switch (result.toLowerCase()) {
            case "white": actual = 1.0; break;
            case "black": actual = 0.0; break;
            default:      actual = 0.5; break;
        }

        // ELO vs BASE opponent
        double expected = 1.0 / (1.0 + Math.pow(10, (BASE - oldRating) / 400.0));
        int newRating   = Math.max(MIN_RATING, (int) Math.round(oldRating + K * (actual - expected)));

        rating.setRating(newRating);
        rating.setGamesPlayed(rating.getGamesPlayed() + 1);

        if (actual == 1.0)      rating.setWins(rating.getWins() + 1);
        else if (actual == 0.0) rating.setLosses(rating.getLosses() + 1);
        else                    rating.setDraws(rating.getDraws() + 1);

        ratingRepository.save(rating);

        System.out.println("Solo rating: " + username + " " + oldRating + " -> " + newRating + " (" + result + ")");

        return new SoloRatingResult(newRating, newRating - oldRating);
    }

    // ================= INNER CLASS - Result DTO =================
    public static class SoloRatingResult {
        public int newRating;
        public int ratingChange;
        public SoloRatingResult(int newRating, int ratingChange) {
            this.newRating    = newRating;
            this.ratingChange = ratingChange;
        }
    }
}