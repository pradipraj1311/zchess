package com.zchess.engine;

public class KingValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        int dr = Math.abs(fr - tr);
        int dc = Math.abs(fc - tc);

        return dr <= 1 && dc <= 1;
    }
}