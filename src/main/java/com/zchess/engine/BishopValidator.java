package com.zchess.engine;

public class BishopValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        if (Math.abs(fr - tr) != Math.abs(fc - tc)) return false;

        int rStep = (tr > fr) ? 1 : -1;
        int cStep = (tc > fc) ? 1 : -1;

        int r = fr + rStep;
        int c = fc + cStep;

        while (r != tr) {
            if (board[r][c] != null && !board[r][c].isEmpty()) return false;
            r += rStep;
            c += cStep;
        }

        return true;
    }
}