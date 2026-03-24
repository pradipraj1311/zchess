package com.zchess.service;

import com.zchess.entity.Game;
import com.zchess.entity.User;
import com.zchess.repository.GameRepository;
import com.zchess.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    // CREATE GAME - logged in user playerWhite bane
    public Game createGame(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = new Game();
        game.setPlayerWhite(user); // game bananaaro = white player
        game.setStatus("Ongoing");
        game.setCurrentTurn("white");

        return gameRepository.save(game);
    }

    // GET ALL GAMES - admin only
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // GET GAME BY ID
    public Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    // GET GAMES BY USER - potani j games
    public List<Game> getGamesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return gameRepository.findByPlayerWhite(user);
    }

    // DELETE GAME - admin only
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}