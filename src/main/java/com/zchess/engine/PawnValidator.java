package com.zchess.engine;


public class PawnValidator {

    // En passant target square (set after double pawn push)
    // format: "row,col" or null
    public static String enPassantTarget = null;

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null) return false;

        boolean isWhite = piece.startsWith("w");
        int direction = isWhite ? -1 : 1;
        String target = board[tr][tc];

        // ── Forward move ──
        if (fc == tc && target == null) {
            // single step
            if (tr - fr == direction) {
                // set en passant cleared (no double push)
                return true;
            }
            // double push from starting rank
            if ((isWhite && fr == 6) || (!isWhite && fr == 1)) {
                if (tr - fr == 2 * direction && board[fr + direction][fc] == null) {
                    return true;
                }
            }
        }

        // ── Diagonal capture ──
        if (Math.abs(fc - tc) == 1 && tr - fr == direction) {
            // normal capture
            if (target != null) return true;

            // en passant capture
            if (enPassantTarget != null) {
                String expected = tr + "," + tc;
                if (enPassantTarget.equals(expected)) return true;
            }
        }

        return false;
    }

    // Call this AFTER a pawn move to update en passant state
    public static void updateEnPassant(String piece, int fr, int fc, int tr, int tc) {
        boolean isWhite = piece.startsWith("w");
        int direction = isWhite ? -1 : 1;

        // double push → set en passant target (the skipped square)
        if (Math.abs(tr - fr) == 2 && fc == tc) {
            enPassantTarget = (fr + direction) + "," + fc;
        } else {
            enPassantTarget = null;
        }
    }

    // Perform en passant capture on board (remove captured pawn)
    public static void applyEnPassant(String[][] board, int fr, int fc, int tr, int tc) {
        String piece = board[fr][fc];
        if (piece == null) return;
        boolean isWhite = piece.startsWith("w");
        int direction = isWhite ? -1 : 1;

        // if diagonal move to empty square = en passant
        if (Math.abs(fc - tc) == 1 && board[tr][tc] == null) {
            // remove the captured pawn (one row behind destination)
            board[tr - direction][tc] = null;
        }
    }
}