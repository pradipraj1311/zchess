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

    // CREATE GAME
    public Game createGame(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Game game = new Game();
        game.setPlayerWhite(user);
        game.setStatus("Ongoing");
        game.setCurrentTurn("white");
        return gameRepository.save(game);
    }

    // UPDATE GAME STATUS - Ongoing → Completed/Draw
    public void updateGameStatus(Long gameId, String result) {
        try {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));

            switch (result) {
                case "WHITE_WIN":
                    game.setStatus("White Won");
                    break;
                case "BLACK_WIN":
                    game.setStatus("Black Won");
                    break;
                case "STALEMATE":
                    game.setStatus("Draw");
                    break;
                default:
                    game.setStatus("Ongoing");
            }
            gameRepository.save(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public List<Game> getGamesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return gameRepository.findByPlayerWhite(user);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}