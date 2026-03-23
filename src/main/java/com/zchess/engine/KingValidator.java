package com.zchess.engine;

public class KingValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];

        // check if piece exists
        if (piece == null) return false;

        // prevent same square move
        if (fr == tr && fc == tc) return false;

        boolean isWhite = piece.startsWith("w");

        int dr = Math.abs(fr - tr);
        int dc = Math.abs(fc - tc);

        // king can move only one square in any direction
        if (dr > 1 || dc > 1) return false;

        String target = board[tr][tc];

        // empty square is valid
        if (target == null) return true;

        // capture only opponent piece
        if (isWhite && target.startsWith("b")) return true;
        if (!isWhite && target.startsWith("w")) return true;

        // cannot capture own piece
        return false;
    }
}