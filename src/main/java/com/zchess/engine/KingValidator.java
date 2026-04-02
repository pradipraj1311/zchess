package com.zchess.engine;

public class KingValidator {

    // Track if kings/rooks have moved (for castling eligibility)
    public static boolean whiteKingMoved  = false;
    public static boolean blackKingMoved  = false;
    public static boolean whiteRookAMoved = false; // a1 rook (queenside)
    public static boolean whiteRookHMoved = false; // h1 rook (kingside)
    public static boolean blackRookAMoved = false; // a8 rook (queenside)
    public static boolean blackRookHMoved = false; // h8 rook (kingside)

    public static void reset() {
        whiteKingMoved = blackKingMoved = false;
        whiteRookAMoved = whiteRookHMoved = false;
        blackRookAMoved = blackRookHMoved = false;
    }

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (piece == null) return false;
        if (fr == tr && fc == tc) return false;

        boolean isWhite = piece.startsWith("w");
        int dr = Math.abs(fr - tr);
        int dc = Math.abs(fc - tc);

        // ── Normal king move (1 square) ──
        if (dr <= 1 && dc <= 1) {
            String target = board[tr][tc];
            if (target == null) return true;
            if (isWhite && target.startsWith("b")) return true;
            if (!isWhite && target.startsWith("w")) return true;
            return false;
        }

        // ── Castling ──
        // King moves exactly 2 squares horizontally, same row
        if (dr == 0 && dc == 2) {
            return canCastle(board, isWhite, fr, fc, tc);
        }

        return false;
    }

    private static boolean canCastle(String[][] board, boolean isWhite, int row, int fc, int tc) {
        // King must not have moved
        if (isWhite && whiteKingMoved)  return false;
        if (!isWhite && blackKingMoved) return false;

        // King must not be in check currently
        if (CheckValidator.isKingInCheck(board, isWhite)) return false;

        boolean kingSide = tc > fc; // moving right = kingside

        if (kingSide) {
            // Kingside: king on e1/e8, rook on h1/h8
            if (isWhite  && whiteRookHMoved) return false;
            if (!isWhite && blackRookHMoved) return false;

            // squares between must be empty
            if (board[row][5] != null || board[row][6] != null) return false;

            // rook must be present
            String rook = isWhite ? "wr" : "br";
            if (!rook.equals(board[row][7])) return false;

            // king must not pass through check
            if (squareUnderAttack(board, row, 5, isWhite)) return false;
            if (squareUnderAttack(board, row, 6, isWhite)) return false;

        } else {
            // Queenside: king on e1/e8, rook on a1/a8
            if (isWhite  && whiteRookAMoved) return false;
            if (!isWhite && blackRookAMoved) return false;

            // squares between must be empty (b,c,d)
            if (board[row][1] != null || board[row][2] != null || board[row][3] != null) return false;

            String rook = isWhite ? "wr" : "br";
            if (!rook.equals(board[row][0])) return false;

            // king must not pass through check
            if (squareUnderAttack(board, row, 3, isWhite)) return false;
            if (squareUnderAttack(board, row, 2, isWhite)) return false;
        }

        return true;
    }

    // Check if a square is under attack by opponent
    private static boolean squareUnderAttack(String[][] board, int r, int c, boolean forWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String p = board[i][j];
                if (p == null) continue;
                boolean isEnemy = forWhite ? p.startsWith("b") : p.startsWith("w");
                if (!isEnemy) continue;
                // Use simple move check (avoid recursion with king)
                if (p.charAt(1) != 'k') {
                    try {
                        if (MoveValidator.isValidMove(p, i, j, r, c, board)) return true;
                    } catch (Exception e) { /* ignore */ }
                }
            }
        }
        return false;
    }

    // Apply castling: move rook too
    public static void applyCastling(String[][] board, boolean isWhite, int row, int fc, int tc) {
        boolean kingSide = tc > fc;
        if (kingSide) {
            // Move rook from h to f
            board[row][5] = board[row][7];
            board[row][7] = null;
        } else {
            // Move rook from a to d
            board[row][3] = board[row][0];
            board[row][0] = null;
        }
    }

    // Update moved flags after a move
    public static void updateMoveFlags(String piece, int fr, int fc) {
        if (piece == null) return;
        switch (piece) {
            case "wk": whiteKingMoved = true; break;
            case "bk": blackKingMoved = true; break;
            case "wr":
                if (fr == 7 && fc == 0) whiteRookAMoved = true;
                if (fr == 7 && fc == 7) whiteRookHMoved = true;
                break;
            case "br":
                if (fr == 0 && fc == 0) blackRookAMoved = true;
                if (fr == 0 && fc == 7) blackRookHMoved = true;
                break;
        }
    }
}