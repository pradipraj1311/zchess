package com.zchess.engine;

public class CheckDetector {

    public static boolean isKingInCheck(String[][] board, char color) {

        int kr = -1;
        int kc = -1;

        String king = color + "k";

        // find king position safely
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                String cell = board[r][c];

                if (cell != null && cell.equals(king)) {
                    kr = r;
                    kc = c;
                }
            }
        }

        // if king not found, return false to avoid crash
        if (kr == -1 || kc == -1) return false;

        char opponent = (color == 'w') ? 'b' : 'w';

        // check all opponent pieces
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                String p = board[r][c];

                if (p == null) continue;

                // check only opponent pieces
                if (p.charAt(0) == opponent) {

                    try {
                        // check if opponent can attack king
                        if (MoveValidator.isValidMove(p, r, c, kr, kc, board)) {
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }
}