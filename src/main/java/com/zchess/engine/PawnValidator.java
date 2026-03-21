package com.zchess.engine;

public class PawnValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null) return false;

        boolean isWhite = piece.startsWith("w");
        int direction = isWhite ? -1 : 1;

        // 1 step forward
        if (fc == tc && board[tr][tc] == null) {
            if (tr - fr == direction) return true;

            // 2 step from start
            if ((isWhite && fr == 6) || (!isWhite && fr == 1)) {
                if (tr - fr == 2 * direction && board[fr + direction][fc] == null) {
                    return true;
                }
            }
        }

        // capture
        if (Math.abs(fc - tc) == 1 && tr - fr == direction) {
            if (board[tr][tc] != null) return true;
        }

        return false;
    }
}