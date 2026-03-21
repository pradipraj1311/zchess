package com.zchess.engine;

public class RookValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        if (fr != tr && fc != tc) return false;

        if (fr == tr) {
            int step = (tc > fc) ? 1 : -1;
            for (int c = fc + step; c != tc; c += step) {
                if (board[fr][c] != null && !board[fr][c].isEmpty()) return false;
            }
        }

        if (fc == tc) {
            int step = (tr > fr) ? 1 : -1;
            for (int r = fr + step; r != tr; r += step) {
                if (board[r][fc] != null && !board[r][fc].isEmpty()) return false;
            }
        }

        return true;
    }
}