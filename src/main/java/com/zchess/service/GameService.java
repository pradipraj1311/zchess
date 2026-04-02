package com.zchess.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zchess.entity.Game;
import com.zchess.entity.User;
import com.zchess.repository.GameRepository;
import com.zchess.repository.UserRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Game createGame(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Game game = new Game();
        game.setPlayerWhite(user);
        game.setStatus("Ongoing");
        game.setCurrentTurn("white");
        return gameRepository.save(game);
    }

    // Update game status in DB - called when game ends
    public void updateGameStatus(Long gameId, String result) {
        try {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));

            String status;
            switch (result) {
                case "WHITE_WIN": status = "White Won";  break;
                case "BLACK_WIN": status = "Black Won";  break;
                case "STALEMATE": status = "Draw";       break;
                default:          status = "Ongoing";
            }

            game.setStatus(status);
            gameRepository.save(game);
            System.out.println("Game " + gameId + " status updated to: " + status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Game> getAllGames() { return gameRepository.findAll(); }

    public Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public List<Game> getGamesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return gameRepository.findByPlayerWhite(user);
    }

    public void deleteGame(Long id) { gameRepository.deleteById(id); }
}