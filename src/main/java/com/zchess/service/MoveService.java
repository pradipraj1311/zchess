package com.zchess.service;

import com.zchess.entity.Move;
import com.zchess.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveService {

    @Autowired
        private MoveRepository moveRepository;

            public Move saveMove(Move move) {
                    return moveRepository.save(move);
                        }

                            public List<Move> getMoves(Long gameId) {
                                    return moveRepository.findAll();
                                        }
                                        }