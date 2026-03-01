package com.zchess.service.engine;

import java.util.List;

public class MoveValidator {

    public static boolean isValidMove(Move move, Board board, String turn) {

            String pieceCode =
                        board.getPiece(move.getFrom().getRow(),
                                                   move.getFrom().getCol());

                                                           if(pieceCode.equals(".")) return false;

                                                                   Piece piece = PieceFactory.createPiece(pieceCode);

                                                                           if(!piece.getColor().equals(turn))
                                                                                       return false;

                                                                                               List<Position> validMoves =
                                                                                                               piece.getValidMoves(move.getFrom(), board);

                                                                                                                       for(Position pos : validMoves) {
                                                                                                                                   if(pos.getRow() == move.getTo().getRow() &&
                                                                                                                                                  pos.getCol() == move.getTo().getCol())
                                                                                                                                                                  return true;
                                                                                                                                                                          }

                                                                                                                                                                                  return false;
                                                                                                                                                                                      }
                                                                                                                                                                                      }