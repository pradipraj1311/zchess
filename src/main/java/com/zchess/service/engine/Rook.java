package com.zchess.service.engine;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(String color) {
            super(color);
                }

                    @Override
                        public List<Position> getValidMoves(Position from, Board board) {
                                return new ArrayList<>();
                                    }
                                    }