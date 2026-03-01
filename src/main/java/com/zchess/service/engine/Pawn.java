package com.zchess.service.engine;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(String color) {
            super(color);
                }

                    @Override
                        public List<Position> getValidMoves(Position from, Board board) {

                                List<Position> moves = new ArrayList<>();

                                        int direction = color.equals("WHITE") ? -1 : 1;

                                                int newRow = from.getRow() + direction;

                                                        if(board.isInside(newRow, from.getCol()) &&
                                                                   board.getPiece(newRow, from.getCol()).equals(".")) {

                                                                               moves.add(new Position(newRow, from.getCol()));
                                                                                       }

                                                                                               return moves;
                                                                                                   }
                                                                                                   }