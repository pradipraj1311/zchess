package com.zchess.service.engine;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(String color) {
            super(color);
                }

                    @Override
                        public List<Position> getValidMoves(Position from, Board board) {
                                return new ArrayList<>();
                                    }
                                    }