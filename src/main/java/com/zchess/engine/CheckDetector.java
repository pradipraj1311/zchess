package com.zchess.engine;

public class CheckDetector {

	public static boolean isKingInCheck(String[][] board, char color) {

		int kr = -1;
		int kc = -1;

		String king = color + "k";

		for (int r = 0; r < 8; r++)
			for (int c = 0; c < 8; c++)
				if (board[r][c].equals(king)) {
					kr = r;
					kc = c;
				}

		char opponent = color == 'w' ? 'b' : 'w';

		for (int r = 0; r < 8; r++) {

			for (int c = 0; c < 8; c++) {

				String p = board[r][c];

				if (p.isEmpty())
					continue;

				if (p.charAt(0) == opponent) {

					if (MoveValidator.isValidMove(p, r, c, kr, kc, board))
						return true;

				}

			}

		}

		return false;

	}

}