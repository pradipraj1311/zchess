package com.zchess.engine;

public class PawnValidator {

    private static boolean isEmpty(String cell) {
        return cell == null || cell.trim().isEmpty();
    }

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc) {

        String piece = board[fr][fc];
        if (isEmpty(piece)) return false;

        boolean isWhite = piece.startsWith("w");
        int direction = isWhite ? -1 : 1;

        String target = board[tr][tc];

        if (fc == tc && isEmpty(target)) {

            if (tr - fr == direction) return true;

            if ((isWhite && fr == 6) || (!isWhite && fr == 1)) {
                if (tr - fr == 2 * direction &&
                        isEmpty(board[fr + direction][fc])) {
                    return true;
                }
            }
        }

        if (Math.abs(fc - tc) == 1 && tr - fr == direction) {
            if (!isEmpty(target)) return true;
        }

        return false;
    }
}