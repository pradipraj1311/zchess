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

            public GameService(GameRepository gameRepository,
                                   UserRepository userRepository) {
                                           this.gameRepository = gameRepository;
                                                   this.userRepository = userRepository;
                                                       }

                                                           public Game createGame(Long userId) {

                                                                   User user = userRepository.findById(userId)
                                                                                   .orElseThrow(() -> new RuntimeException("User not found"));

                                                                                           Game game = new Game();
                                                                                                   game.setCreator(user);

                                                                                                           return gameRepository.save(game);
                                                                                                               }

                                                                                                                   public List<Game> getAllGames() {
                                                                                                                           return gameRepository.findAll();
                                                                                                                               }

                                                                                                                                   public List<Game> getGamesByUser(Long userId) {
                                                                                                                                           return gameRepository.findByCreatorId(userId);
                                                                                                                                               }

                                                                                                                                                   public void deleteGame(Long id) {
                                                                                                                                                           gameRepository.deleteById(id);
                                                                                                                                                               }
                                                                                                                                                               }