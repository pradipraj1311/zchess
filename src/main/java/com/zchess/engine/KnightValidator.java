package com.zchess.engine;

public class KnightValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];

        // check if piece exists
        if (piece == null) return false;

        // prevent same square move
        if (fr == tr && fc == tc) return false;

        boolean isWhite = piece.startsWith("w");

        int dr = Math.abs(fr - tr);
        int dc = Math.abs(fc - tc);

        // valid L-shape move
        if (!((dr == 2 && dc == 1) || (dr == 1 && dc == 2))) return false;

        String target = board[tr][tc];

        // empty square is valid
        if (target == null) return true;

        // capture only if opponent piece
        if (isWhite && target.startsWith("b")) return true;
        if (!isWhite && target.startsWith("w")) return true;

        // cannot capture own piece
        return false;
    }
}