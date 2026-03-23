package com.zchess.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zchess.engine.Board;
import com.zchess.engine.CheckValidator;
import com.zchess.engine.ChessNotation;
import com.zchess.engine.GameState;
import com.zchess.engine.MoveValidator;
import com.zchess.entity.Move;

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

    // ================= MOVE =================
    // NOW returns boolean: true = valid move, false = invalid
    public boolean move(Long gameId, Move move) {

        String[][] board = Board.getBoard();

        int fr = move.getFromRow();
        int fc = move.getFromCol();
        int tr = move.getToRow();
        int tc = move.getToCol();

        String piece = board[fr][fc];

        // no piece at source
        if (piece == null) return false;

        boolean isWhite = piece.startsWith("w");

        // turn validation
        if (isWhite && !GameState.currentTurn.equals("white")) return false;
        if (!isWhite && !GameState.currentTurn.equals("black")) return false;

        String target = board[tr][tc];

        // prevent own capture
        if (target != null && target.startsWith(piece.substring(0, 1))) return false;

        // validate move
        try {
            if (!MoveValidator.isValidMove(piece, fr, fc, tr, tc, board)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // apply move temporarily
        board[tr][tc] = piece;
        board[fr][fc] = null;

        // king safety check - rollback if king in check
        try {
            if (CheckValidator.isKingInCheck(board, isWhite)) {
                board[fr][fc] = piece;
                board[tr][tc] = target;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            board[fr][fc] = piece;
            board[tr][tc] = target;
            return false;
        }

        // pawn promotion
        if (piece.equals("wp") && tr == 0) board[tr][tc] = "wq";
        if (piece.equals("bp") && tr == 7) board[tr][tc] = "bq";

        // save move (store promoted piece if needed)
        move.setPiece(board[tr][tc]);
        moves.add(move);

        // notation
        boolean isCapture = (target != null);
        String notation = ChessNotation.convert(piece, fr, fc, tr, tc, isCapture);

        // check detection for opponent
        boolean opponentIsWhite = !isWhite;
        try {
            if (CheckValidator.isKingInCheck(board, opponentIsWhite)) {
                notation += "+";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // save history
        if (isWhite) {
            whiteHistory.add(notation);
        } else {
            blackHistory.add(notation);
        }

        System.out.println("MOVE: " + fr + "," + fc + " -> " + tr + "," + tc);
        System.out.println("PIECE: " + piece);

        // switch turn
        GameState.switchTurn();

        return true;
    }

    // ================= UNDO =================
    public void undo() {

        if (moves.isEmpty()) return;

        Move lastMove = moves.remove(moves.size() - 1);

        // remove from history (switch turn first to know who moved last)
        if (GameState.currentTurn.equals("white")) {
            // black just moved
            if (!blackHistory.isEmpty()) blackHistory.remove(blackHistory.size() - 1);
        } else {
            // white just moved
            if (!whiteHistory.isEmpty()) whiteHistory.remove(whiteHistory.size() - 1);
        }

        // rebuild board from scratch
        Board.resetBoard();
        String[][] board = Board.getBoard();

        for (Move m : moves) {
            board[m.getToRow()][m.getToCol()] = m.getPiece();
            board[m.getFromRow()][m.getFromCol()] = null;
        }

        GameState.switchTurn();
    }

    // ================= RESET =================
    public void reset() {
        moves.clear();
        whiteHistory.clear();
        blackHistory.clear();
        Board.resetBoard();
        GameState.reset();
    }
}