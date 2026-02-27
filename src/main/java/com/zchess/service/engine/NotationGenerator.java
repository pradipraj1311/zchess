package com.zchess.service.engine;

import com.zchess.entity.*;

public class NotationGenerator {

    public String generate(Game game, Piece piece, int to) {

            char file = (char) ('a' + (to % 8));
                    int rank = 8 - (to / 8);

                            return piece.getType().name().charAt(0) + "" + file + rank;
                                }
                                }