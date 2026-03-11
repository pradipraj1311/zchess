package com.zchess.service;

import com.zchess.engine.Board;
import com.zchess.entity.Move;
import org.springframework.stereotype.Service;

@Service
public class MoveService {

    public String[][] saveMove(Move move){

            String[][] board = Board.getBoard();

                    int fr = move.getFromRow();
                            int fc = move.getFromCol();
                                    int tr = move.getToRow();
                                            int tc = move.getToCol();

                                                    String piece = board[fr][fc];

                                                            if(piece == null || piece.equals("")){
                                                                        return board;
                                                                                }

                                                                                        board[tr][tc] = piece;
                                                                                                board[fr][fc] = "";

                                                                                                        return board;
                                                                                                            }
                                                                                                            }