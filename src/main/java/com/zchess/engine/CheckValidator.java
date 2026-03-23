package com.zchess.engine;

public class CheckValidator {

    public static boolean isKingInCheck(String[][] board, boolean whiteKing) {

        // identify king
        String king = whiteKing ? "wk" : "bk";

        int kr = -1;
        int kc = -1;

        // find king position
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (king.equals(board[r][c])) {
                    kr = r;
                    kc = c;
                    break;
                }
            }
        }

        // if king not found, avoid crash
        if (kr == -1 || kc == -1) return false;

        // check all enemy pieces
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                String piece = board[r][c];

                if (piece == null) continue;

                // check only opponent pieces
                boolean enemy = whiteKing ? piece.startsWith("b") : piece.startsWith("w");

                if (!enemy) continue;

                try {
                    // if any enemy piece can attack king
                    if (MoveValidator.isValidMove(piece, r, c, kr, kc, board)) {
                        return true;
                    }
                } catch (Exception e) {
                    // prevent crash from any validator
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}