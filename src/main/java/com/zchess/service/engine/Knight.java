package com.zchess.service.engine;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(String color) {
            super(color);
                }

                    @Override
                        public List<Position> getValidMoves(Position from, Board board) {
                                return new ArrayList<>();
                                    }
                                    }