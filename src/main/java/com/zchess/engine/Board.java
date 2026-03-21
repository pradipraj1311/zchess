package com.zchess.engine;

public class Board {

	private static String[][] board;

	static {
		resetBoard();
	}

	public static String[][] getBoard() {
		return board;
	}

	public static void resetBoard() {

		board = new String[][] {

				{ "br", "bn", "bb", "bq", "bk", "bb", "bn", "br" }, { "bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp" },
				{ "", "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "", "" },
				{ "wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp" }, { "wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr" }

		};
	}
}