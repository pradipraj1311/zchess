package com.zchess.engine;


public class CheckmateDetector {

    // check if the given color has NO legal moves left
    public static boolean hasNoLegalMoves(String[][] board, boolean isWhite) {

        for (int fr = 0; fr < 8; fr++) {
            for (int fc = 0; fc < 8; fc++) {

                String piece = board[fr][fc];
                if (piece == null) continue;
                if (isWhite && !piece.startsWith("w")) continue;
                if (!isWhite && !piece.startsWith("b")) continue;

                // try every possible destination
                for (int tr = 0; tr < 8; tr++) {
                    for (int tc = 0; tc < 8; tc++) {

                        if (fr == tr && fc == tc) continue;

                        // check if move is valid
                        try {
                            if (!MoveValidator.isValidMove(piece, fr, fc, tr, tc, board)) continue;
                        } catch (Exception e) {
                            continue;
                        }

                        String target = board[tr][tc];

                        // prevent capturing own piece
                        if (target != null && target.startsWith(piece.substring(0, 1))) continue;

                        // simulate move
                        board[tr][tc] = piece;
                        board[fr][fc] = null;

                        boolean stillInCheck = false;
                        try {
                            stillInCheck = CheckValidator.isKingInCheck(board, isWhite);
                        } catch (Exception e) {
                            // ignore
                        }

                        // undo move
                        board[fr][fc] = piece;
                        board[tr][tc] = target;

                        // found at least one legal move - not checkmate/stalemate
                        if (!stillInCheck) return false;
                    }
                }
            }
        }

        // no legal move found
        return true;
    }

    // CHECKMATE: king in check + no legal moves
    public static boolean isCheckmate(String[][] board, boolean isWhite) {
        return CheckValidator.isKingInCheck(board, isWhite) && hasNoLegalMoves(board, isWhite);
    }

    // STALEMATE: king NOT in check + no legal moves
    public static boolean isStalemate(String[][] board, boolean isWhite) {
        return !CheckValidator.isKingInCheck(board, isWhite) && hasNoLegalMoves(board, isWhite);
    }
}