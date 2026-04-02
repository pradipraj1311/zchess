package com.zchess.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zchess.entity.User;
import com.zchess.repository.GameRepository;
import com.zchess.repository.MoveRepository;
import com.zchess.repository.RatingRepository;
import com.zchess.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository   userRepository;
    private final PasswordEncoder  passwordEncoder;
    private final RatingService    ratingService;
    private final RatingRepository ratingRepository;
    private final GameRepository   gameRepository;
    private final MoveRepository   moveRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RatingService ratingService,
                       RatingRepository ratingRepository,
                       GameRepository gameRepository,
                       MoveRepository moveRepository) {
        this.userRepository   = userRepository;
        this.passwordEncoder  = passwordEncoder;
        this.ratingService    = ratingService;
        this.ratingRepository = ratingRepository;
        this.gameRepository   = gameRepository;
        this.moveRepository   = moveRepository;
    }

    // Register - encode password + create rating
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        ratingService.createRating(saved);
        return saved;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // DELETE USER - cascade: moves → games → ratings → user
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        System.out.println("Deleting user: " + user.getUsername());

        // 1. Delete moves linked to user's games
        try {
            List<com.zchess.entity.Game> games = gameRepository.findByPlayerWhite(user);
            for (com.zchess.entity.Game game : games) {
                moveRepository.deleteByGame(game);
                System.out.println("Deleted moves for game: " + game.getId());
            }
        } catch (Exception e) {
            System.err.println("Error deleting moves: " + e.getMessage());
        }

        // 2. Delete games
        try {
            gameRepository.deleteByPlayerWhite(user);
            System.out.println("Deleted games for user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("Error deleting games: " + e.getMessage());
        }

        // 3. Delete rating
        try {
            ratingRepository.findByUser(user).ifPresent(r -> {
                ratingRepository.delete(r);
                System.out.println("Deleted rating for: " + user.getUsername());
            });
        } catch (Exception e) {
            System.err.println("Error deleting rating: " + e.getMessage());
        }

        // 4. Finally delete user
        userRepository.deleteById(id);
        System.out.println("User deleted: " + user.getUsername());
    }
}