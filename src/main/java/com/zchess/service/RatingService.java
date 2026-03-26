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
    private final UserRepository userRepository;

    // ELO K-factor - nava players mate 32, experienced mate 16
    private static final int K_FACTOR = 32;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    // navo user register thay tyare rating create karo (1000 thi sharu)
    public Rating createRating(User user) {
        Rating rating = new Rating(user);
        return ratingRepository.save(rating);
    }

    // user ni rating get karo - na hoy to create karo
    public Rating getRating(User user) {
        return ratingRepository.findByUser(user)
                .orElseGet(() -> createRating(user));
    }

    // username thi rating get karo
    public Rating getRatingByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getRating(user);
    }

    // badha users ni ratings - leaderboard mate
    public List<Rating> getAllRatings() {
        return ratingRepository.findAllByOrderByRatingDesc();
    }

    // ================= ELO CALCULATION =================
    // result: 1.0 = white jityo, 0.0 = black jityo, 0.5 = draw
    public void updateRatings(String whiteUsername, String blackUsername, double result) {

        User whiteUser = userRepository.findByUsername(whiteUsername)
                .orElseThrow(() -> new RuntimeException("White player not found"));
        User blackUser = userRepository.findByUsername(blackUsername)
                .orElseThrow(() -> new RuntimeException("Black player not found"));

        Rating whiteRating = getRating(whiteUser);
        Rating blackRating = getRating(blackUser);

        int rA = whiteRating.getRating();
        int rB = blackRating.getRating();

        // ELO expected score calculate karo
        // E_A = 1 / (1 + 10^((R_B - R_A) / 400))
        double expectedWhite = 1.0 / (1.0 + Math.pow(10, (rB - rA) / 400.0));
        double expectedBlack = 1.0 / (1.0 + Math.pow(10, (rA - rB) / 400.0));

        // actual score
        double actualWhite = result;        // 1.0=win, 0.5=draw, 0.0=loss
        double actualBlack = 1.0 - result;  // opposite

        // new rating = old + K * (actual - expected)
        int newWhiteRating = (int) Math.round(rA + K_FACTOR * (actualWhite - expectedWhite));
        int newBlackRating = (int) Math.round(rB + K_FACTOR * (actualBlack - expectedBlack));

        // minimum rating 100 thi niche na jay
        newWhiteRating = Math.max(newWhiteRating, 100);
        newBlackRating = Math.max(newBlackRating, 100);

        // white update
        whiteRating.setRating(newWhiteRating);
        whiteRating.setGamesPlayed(whiteRating.getGamesPlayed() + 1);
        if (result == 1.0) whiteRating.setWins(whiteRating.getWins() + 1);
        else if (result == 0.0) whiteRating.setLosses(whiteRating.getLosses() + 1);
        else whiteRating.setDraws(whiteRating.getDraws() + 1);

        // black update
        blackRating.setRating(newBlackRating);
        blackRating.setGamesPlayed(blackRating.getGamesPlayed() + 1);
        if (result == 0.0) blackRating.setWins(blackRating.getWins() + 1);
        else if (result == 1.0) blackRating.setLosses(blackRating.getLosses() + 1);
        else blackRating.setDraws(blackRating.getDraws() + 1);

        ratingRepository.save(whiteRating);
        ratingRepository.save(blackRating);

        System.out.println("Rating updated: " + whiteUsername + " " + rA + " -> " + newWhiteRating);
        System.out.println("Rating updated: " + blackUsername + " " + rB + " -> " + newBlackRating);
    }
}