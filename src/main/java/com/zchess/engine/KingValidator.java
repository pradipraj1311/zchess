package com.zchess.engine;

public class KingValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null || piece.equals("")) return false;

        boolean isWhite = piece.startsWith("w");

        int dr = Math.abs(fr - tr);
        int dc = Math.abs(fc - tc);

        //  basic king move
        if (dr > 1 || dc > 1) return false;

        //  destination check
        String target = board[tr][tc];

        // empty → valid
        if (target == null || target.equals("")) return true;

        // enemy → valid
        boolean enemy = isWhite ? target.startsWith("b") : target.startsWith("w");
        return enemy;
    }
}