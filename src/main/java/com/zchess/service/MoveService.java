package com.zchess.service;

import com.zchess.entity.Move;
import com.zchess.repository.MoveRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveService {

    private final MoveRepository moveRepository;

        public MoveService(MoveRepository moveRepository){
                this.moveRepository = moveRepository;
                    }

                        public Move saveMove(Move move){
                                return moveRepository.save(move);
                                    }

                                        public List<Move> getMoves(Long gameId){
                                                return moveRepository.findByGameId(gameId);
                                                    }
                                                    }