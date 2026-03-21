package com.zchess.engine;

public class BishopValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null || piece.equals("")) return false;

        boolean isWhite = piece.startsWith("w");

        // 🔹 diagonal check
        if (Math.abs(fr - tr) != Math.abs(fc - tc)) return false;

        int rStep = (tr > fr) ? 1 : -1;
        int cStep = (tc > fc) ? 1 : -1;

        int r = fr + rStep;
        int c = fc + cStep;

        // 🔹 path clear check
        while (r != tr) {
            if (board[r][c] != null && !board[r][c].equals("")) return false;
            r += rStep;
            c += cStep;
        }

        // 🔹 destination check
        String target = board[tr][tc];

        // empty → valid
        if (target == null || target.equals("")) return true;

        // enemy → valid
        boolean enemy = isWhite ? target.startsWith("b") : target.startsWith("w");
        return enemy;
    }
}