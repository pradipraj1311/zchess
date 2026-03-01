package com.zchess.service.engine;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(String color) {
            super(color);
                }

                    @Override
                        public List<Position> getValidMoves(Position from, Board board) {
                                return new ArrayList<>();
                                    }
                                    }