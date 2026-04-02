package com.zchess.engine;


public class Board {

    // main chess board (8x8)
    private static String[][] board;

    // initialize board when class loads
    static {
        resetBoard();
    }

    // return current board
    public static String[][] getBoard() {
        return board;
    }

    // reset board to initial chess position
    public static void resetBoard() {

        board = new String[][] {

            // black pieces
            { "br", "bn", "bb", "bq", "bk", "bb", "bn", "br" },
            { "bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp" },

            // empty squares (use null)
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },

            // white pieces
            { "wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp" },
            { "wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr" }
        };
    }
}