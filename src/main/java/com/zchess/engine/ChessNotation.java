package com.zchess.engine;

public class ChessNotation {

    public static String convert(String piece, int fr, int fc, int tr, int tc, boolean capture) {

        char file = (char) ('a' + tc);
        int rank = 8 - tr;

        String square = "" + file + rank;

        // Pawn
        if (piece.charAt(1) == 'p') {

            if (capture) {
                char fromFile = (char) ('a' + fc);
                return fromFile + "x" + square;
            }

            return square;
        }

        String letter = switch (piece.charAt(1)) {
            case 'r' -> "R";
            case 'n' -> "N";
            case 'b' -> "B";
            case 'q' -> "Q";
            case 'k' -> "K";
            default -> "";
        };

        if (capture) return letter + "x" + square;

        return letter + square;
    }
}