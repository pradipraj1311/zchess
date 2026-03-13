package com.zchess.service;

import com.zchess.engine.Board;
import com.zchess.engine.GameState;
import com.zchess.engine.MoveValidator;
import com.zchess.engine.CheckDetector;
import com.zchess.engine.ChessNotation;
import com.zchess.entity.Move;

import org.springframework.stereotype.Service;

@Service
public class MoveService {

    public String[][] saveMove(Move move){

            String[][] board=Board.getBoard();

                    int fr=move.getFromRow();
                            int fc=move.getFromCol();
                                    int tr=move.getToRow();
                                            int tc=move.getToCol();

                                                    String piece=board[fr][fc];

                                                            if(piece.equals(""))
                                                                        return board;

                                                                                char color=piece.charAt(0);

                                                                                        if(GameState.turn=='w' && color!='w')
                                                                                                    return board;

                                                                                                            if(GameState.turn=='b' && color!='b')
                                                                                                                        return board;

                                                                                                                                boolean valid = MoveValidator.isValidMove(piece,fr,fc,tr,tc,board);

                                                                                                                                        if(!valid)
                                                                                                                                                    return board;
                                                                                                                                                String captured=board[tr][tc];

                                                                                                                                                board[tr][tc]=piece;
                                                                                                                                                board[fr][fc]="";

                                                                                                                                                if(CheckDetector.isKingInCheck(board,color)){

                                                                                                                                                    board[fr][fc]=piece;
                                                                                                                                                        board[tr][tc]=captured;

                                                                                                                                                            return board;

                                                                                                                                                            }

                                                                                                                                                            board[tr][tc]=piece;
                                                                                                                                                                    board[fr][fc]="";

                                                                                                                                                                            String notation=ChessNotation.convert(piece,fr,fc,tr,tc);

                                                                                                                                                                                    GameState.addMove(notation,board);

                                                                                                                                                                                            GameState.turn=(GameState.turn=='w')?'b':'w';

                                                                                                                                                                                                    return board;
                                                                                                                                                                                                        }

                                                                                                                                                                                                        }