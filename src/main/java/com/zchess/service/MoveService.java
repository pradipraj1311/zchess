package com.zchess.service;

import com.zchess.engine.Board;
import com.zchess.engine.MoveValidator;
import com.zchess.entity.Move;
import com.zchess.repository.MoveRepository;
import org.springframework.stereotype.Service;

@Service
public class MoveService {

    private final MoveRepository moveRepository;

        public MoveService(MoveRepository moveRepository) {
                this.moveRepository = moveRepository;
                    }

                        public String[][] saveMove(Move move) {

                                String[][] board = Board.getBoard();

                                        int fr = move.getFromRow();
                                                int fc = move.getFromCol();
                                                        int tr = move.getToRow();
                                                                int tc = move.getToCol();

                                                                        // validate move
                                                                                boolean valid = MoveValidator.isValid(board, fr, fc, tr, tc);

                                                                                        if (!valid) {
                                                                                                    throw new RuntimeException("Invalid chess move");
                                                                                                            }

                                                                                                                    // move piece
                                                                                                                            board[tr][tc] = board[fr][fc];
                                                                                                                                    board[fr][fc] = "";

                                                                                                                                            // save to database
                                                                                                                                                    moveRepository.save(move);

                                                                                                                                                            return board;
                                                                                                                                                                }

                                                                                                                                                                    public String[][] getBoard() {
                                                                                                                                                                            return Board.getBoard();
                                                                                                                                                                                }
                                                                                                                                                                                }