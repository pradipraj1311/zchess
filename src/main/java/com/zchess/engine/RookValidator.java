package com.zchess.engine;

public class RookValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null || piece.equals("")) return false;

        boolean isWhite = piece.startsWith("w");

        // straight move only
        if (fr != tr && fc != tc) return false;

        //  horizontal
        if (fr == tr) {
            int step = (tc > fc) ? 1 : -1;
            for (int c = fc + step; c != tc; c += step) {
                if (board[fr][c] != null && !board[fr][c].equals("")) return false;
            }
        }

        //  vertical
        if (fc == tc) {
            int step = (tr > fr) ? 1 : -1;
            for (int r = fr + step; r != tr; r += step) {
                if (board[r][fc] != null && !board[r][fc].equals("")) return false;
            }
        }

        //  destination check
        String target = board[tr][tc];

        // empty → valid
        if (target == null || target.equals("")) return true;

        // enemy → valid
        boolean enemy = isWhite ? target.startsWith("b") : target.startsWith("w");
        return enemy;
    }
}