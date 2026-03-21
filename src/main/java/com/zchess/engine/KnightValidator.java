package com.zchess.engine;

public class KnightValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null || piece.equals("")) return false;

        boolean isWhite = piece.startsWith("w");

        int dr = Math.abs(fr - tr);
        int dc = Math.abs(fc - tc);

        //  L shape check
        if (!((dr == 2 && dc == 1) || (dr == 1 && dc == 2))) return false;

        // destination check
        String target = board[tr][tc];

        // empty → valid
        if (target == null || target.equals("")) return true;

        // enemy → valid
        boolean enemy = isWhite ? target.startsWith("b") : target.startsWith("w");
        return enemy;
    }
}