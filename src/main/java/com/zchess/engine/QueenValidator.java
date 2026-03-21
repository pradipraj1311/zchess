package com.zchess.engine;

public class QueenValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null || piece.equals("")) return false;

        //  rook movement
        if (fr == tr || fc == tc) {
            return RookValidator.isValid(board, fr, fc, tr, tc);
        }

        // bishop movement
        if (Math.abs(fr - tr) == Math.abs(fc - tc)) {
            return BishopValidator.isValid(board, fr, fc, tr, tc);
        }

        return false;
    }
}