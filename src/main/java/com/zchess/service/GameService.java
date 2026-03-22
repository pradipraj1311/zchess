package com.zchess.service;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // CREATE GAME
    public Game createGame(Game game) {

        if (game.getStatus() == null) {
            game.setStatus("ONGOING");
        }

        if (game.getCurrentTurn() == null) {
            game.setCurrentTurn("white");
        }

        return gameRepository.save(game);
    }

    //  GET ALL GAMES
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    //  GET GAME BY ID
    public Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    //  DELETE GAME
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}