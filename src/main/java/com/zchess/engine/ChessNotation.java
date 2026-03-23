package com.zchess.engine;

public class ChessNotation {

    public static String convert(String piece, int fr, int fc, int tr, int tc, boolean capture) {

        // convert column index to file 
        char file = (char) ('a' + tc);

        // convert row index to rank 
        int rank = 8 - tr;

        // destination square 
        String square = "" + file + rank;

        // handle pawn moves
        if (piece.charAt(1) == 'p') {

            // pawn capture 
            if (capture) {
                char fromFile = (char) ('a' + fc);
                return fromFile + "x" + square;
            }

            // normal pawn move 
            return square;
        }

        // piece notation
        String letter = switch (piece.charAt(1)) {
            case 'r' -> "R";
            case 'n' -> "N";
            case 'b' -> "B";
            case 'q' -> "Q";
            case 'k' -> "K";
            default -> "";
        };

        // capture move
        if (capture) return letter + "x" + square;

        // normal move 
        return letter + square;
    }
}