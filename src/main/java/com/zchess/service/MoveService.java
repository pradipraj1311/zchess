package com.zchess.service;

import com.zchess.engine.*;
import com.zchess.entity.Move;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveService {

    private static List<String> whiteHistory = new ArrayList<>();
    private static List<String> blackHistory = new ArrayList<>();
    private static List<Move> moves = new ArrayList<>();

    public String[][] getBoard() {
        return Board.getBoard();
    }

    public List<String> getWhiteHistory() {
        return whiteHistory;
    }

    public List<String> getBlackHistory() {
        return blackHistory;
    }

    public String getTurn() {
        return GameState.currentTurn;
    }

    public String[][] move(Move move) {

        String[][] board = Board.getBoard();

        int fr = move.getFromRow();
        int fc = move.getFromCol();
        int tr = move.getToRow();
        int tc = move.getToCol();

        String piece = board[fr][fc];

        if (piece == null) return board;

        boolean isWhite = piece.startsWith("w");

        // turn check
        if (isWhite && !GameState.currentTurn.equals("white")) return board;
        if (!isWhite && !GameState.currentTurn.equals("black")) return board;

        String target = board[tr][tc];

        // prevent own capture
        if (target != null && target.startsWith(piece.substring(0,1))) {
            return board;
        }

        // validate move
        if (!MoveValidator.isValidMove(piece, fr, fc, tr, tc, board)) {
            return board;
        }

        // apply move
        board[tr][tc] = piece;
        board[fr][fc] = null;

        // king safety
        if (CheckValidator.isKingInCheck(board, isWhite)) {
            board[fr][fc] = piece;
            board[tr][tc] = target;
            return board;
        }

        // pawn promotion
        if (piece.equals("wp") && tr == 0) board[tr][tc] = "wq";
        if (piece.equals("bp") && tr == 7) board[tr][tc] = "bq";

        // save move
        move.setPiece(piece);
        moves.add(move);

        boolean capture = (target != null);

        String notation = ChessNotation.convert(piece, fr, fc, tr, tc, capture);

        if (GameState.currentTurn.equals("white")) {
            whiteHistory.add(notation);
        } else {
            blackHistory.add(notation);
        }

        switchTurn();

        return board;
    }

    public void undo() {

        if (moves.isEmpty()) return;

        moves.remove(moves.size() - 1);

        if (GameState.currentTurn.equals("black") && !whiteHistory.isEmpty()) {
            whiteHistory.remove(whiteHistory.size() - 1);
        } else if (!blackHistory.isEmpty()) {
            blackHistory.remove(blackHistory.size() - 1);
        }

        Board.resetBoard();

        String[][] board = Board.getBoard();

        for (Move m : moves) {
            board[m.getToRow()][m.getToCol()] = m.getPiece();
            board[m.getFromRow()][m.getFromCol()] = null;
        }

        switchTurn();
    }

    public void reset() {

        moves.clear();
        whiteHistory.clear();
        blackHistory.clear();

        Board.resetBoard();
        GameState.currentTurn = "white";
    }

    private void switchTurn() {

        if (GameState.currentTurn.equals("white"))
            GameState.currentTurn = "black";
        else
            GameState.currentTurn = "white";
    }
}