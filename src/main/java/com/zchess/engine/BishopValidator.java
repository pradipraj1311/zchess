package com.zchess.engine;


public class BishopValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];

        // check if piece exists
        if (piece == null) return false;

        // prevent same square move
        if (fr == tr && fc == tc) return false;

        boolean isWhite = piece.startsWith("w");

        // bishop moves only diagonally
        if (Math.abs(fr - tr) != Math.abs(fc - tc)) return false;

        int rStep = (tr > fr) ? 1 : -1;
        int cStep = (tc > fc) ? 1 : -1;

        int r = fr + rStep;
        int c = fc + cStep;

        // check if path is clear
        while (r != tr) {
            if (board[r][c] != null) return false;
            r += rStep;
            c += cStep;
        }

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