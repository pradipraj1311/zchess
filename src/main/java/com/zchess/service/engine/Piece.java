package com.zchess.service.engine;

import java.util.List;

public abstract class Piece {

    protected String color;

        public Piece(String color) {
                this.color = color;
                    }

                        public String getColor() {
                                return color;
                                    }

                                        public abstract List<Position> getValidMoves(Position from, Board board);
                                        }