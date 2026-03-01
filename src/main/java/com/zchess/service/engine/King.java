package com.zchess.service.engine;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(String color) {
            super(color);
                }

                    @Override
                        public List<Position> getValidMoves(Position from, Board board) {
                                return new ArrayList<>();
                                    }
                                    }