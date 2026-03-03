package com.zchess.service;

import com.zchess.entity.Game;
import com.zchess.entity.Move;
import com.zchess.repository.GameRepository;
import com.zchess.repository.MoveRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MoveService {

    private final MoveRepository moveRepository;
        private final GameRepository gameRepository;

            public MoveService(MoveRepository moveRepository,
                                   GameRepository gameRepository) {
                                           this.moveRepository = moveRepository;
                                                   this.gameRepository = gameRepository;
                                                       }

                                                           public Move addMove(Long gameId, String from, String to, int moveNumber) {

                                                                   Game game = gameRepository.findById(gameId)
                                                                                   .orElseThrow(() -> new RuntimeException("Game not found"));

                                                                                           Move move = new Move();
                                                                                                   move.setGame(game);
                                                                                                           move.setFromPosition(from);
                                                                                                                   move.setToPosition(to);
                                                                                                                           move.setMoveNumber(moveNumber);

                                                                                                                                   return moveRepository.save(move);
                                                                                                                                       }

                                                                                                                                           public List<Move> getMovesByGame(Long gameId) {
                                                                                                                                                   return moveRepository.findByGameId(gameId);
                                                                                                                                                       }
                                                                                                                                                       }