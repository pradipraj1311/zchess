package com.zchess.engine;

public class RookValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];

        // no piece
        if (piece == null) return false;

        //  same square move not allowed
        if (fr == tr && fc == tc) return false;

        boolean isWhite = piece.startsWith("w");

        //  only straight line move allowed
        if (fr != tr && fc != tc) return false;

        //  horizontal movement
        if (fr == tr) {
            int step = (tc > fc) ? 1 : -1;

            for (int c = fc + step; c != tc; c += step) {
                if (board[fr][c] != null) return false; // path blocked
            }
        }

        //  vertical movement
        if (fc == tc) {
            int step = (tr > fr) ? 1 : -1;

            for (int r = fr + step; r != tr; r += step) {
                if (board[r][fc] != null) return false; // path blocked
            }
        }

        //  destination check
        String target = board[tr][tc];

        // empty square → valid
        if (target == null) return true;

        // enemy capture → valid
        if (isWhite && target.startsWith("b")) return true;
        if (!isWhite && target.startsWith("w")) return true;

        // own piece
        return false;
    }
}