package com.zchess.service;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

        public GameService(GameRepository gameRepository){
                this.gameRepository = gameRepository;
                    }

                        public Game createGame(){
                                return gameRepository.save(new Game());
                                    }

                                        public List<Game> getAllGames(){
                                                return gameRepository.findAll();
                                                    }

                                                        public void deleteGame(Long id){
                                                                gameRepository.deleteById(id);
                                                                    }
                                                                    }