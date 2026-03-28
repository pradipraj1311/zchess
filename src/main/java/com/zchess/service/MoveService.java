package com.zchess.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zchess.engine.Board;
import com.zchess.engine.CheckValidator;
import com.zchess.engine.CheckmateDetector;
import com.zchess.engine.ChessNotation;
import com.zchess.engine.GameState;
import com.zchess.engine.MoveValidator;
import com.zchess.entity.Game;
import com.zchess.entity.Move;
import com.zchess.repository.GameRepository;
import com.zchess.repository.MoveRepository;

@Service
public class MoveService {

    private static List<String> whiteHistory = new ArrayList<>();
    private static List<String> blackHistory  = new ArrayList<>();
    private static List<Move>   moves         = new ArrayList<>();
    private static int          moveNumber    = 0;
    private static String       gameResult    = "ONGOING";
    private static Long         currentGameId = null;

    private final MoveRepository moveRepository;
    private final GameRepository gameRepository;
    private final GameService    gameService;

    public MoveService(MoveRepository moveRepository,
                       GameRepository gameRepository,
                       GameService gameService) {
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.gameService    = gameService;
    }

    public String[][]   getBoard()        { return Board.getBoard(); }
    public List<String> getWhiteHistory() { return whiteHistory; }
    public List<String> getBlackHistory() { return blackHistory; }
    public String       getTurn()         { return GameState.currentTurn; }
    public String       getGameResult()   { return gameResult; }

    // ================= MOVE =================
    public String move(Long gameId, Move move) {

        // NEW USER LOGIN - different gameId = reset board state
        if (currentGameId != null && !currentGameId.equals(gameId)) {
            resetState();
        }
        currentGameId = gameId;

        if (!gameResult.equals("ONGOING")) return gameResult;

        String[][] board = Board.getBoard();
        int fr = move.getFromRow(), fc = move.getFromCol();
        int tr = move.getToRow(),   tc = move.getToCol();

        String piece = board[fr][fc];
        if (piece == null) return "INVALID";

        boolean isWhite = piece.startsWith("w");

        if (isWhite  && !GameState.currentTurn.equals("white")) return "INVALID";
        if (!isWhite && !GameState.currentTurn.equals("black")) return "INVALID";

        String target = board[tr][tc];
        if (target != null && target.startsWith(piece.substring(0, 1))) return "INVALID";

        try {
            if (!MoveValidator.isValidMove(piece, fr, fc, tr, tc, board)) return "INVALID";
        } catch (Exception e) { e.printStackTrace(); return "INVALID"; }

        // apply move
        board[tr][tc] = piece;
        board[fr][fc] = null;

        // king safety rollback
        try {
            if (CheckValidator.isKingInCheck(board, isWhite)) {
                board[fr][fc] = piece; board[tr][tc] = target;
                return "INVALID";
            }
        } catch (Exception e) {
            board[fr][fc] = piece; board[tr][tc] = target;
            return "INVALID";
        }

        // pawn promotion
        if (piece.equals("wp") && tr == 0) board[tr][tc] = "wq";
        if (piece.equals("bp") && tr == 7) board[tr][tc] = "bq";

        // notation
        boolean isCapture   = (target != null);
        String  notation    = ChessNotation.convert(piece, fr, fc, tr, tc, isCapture);

        // checkmate / stalemate detection
        boolean opponentIsWhite = !isWhite;
        boolean isCheckmate = false, isStalemate = false, isCheck = false;
        try {
            isCheckmate = CheckmateDetector.isCheckmate(board, opponentIsWhite);
            isStalemate = CheckmateDetector.isStalemate(board, opponentIsWhite);
            isCheck     = CheckValidator.isKingInCheck(board, opponentIsWhite);
        } catch (Exception e) { e.printStackTrace(); }

        if (isCheckmate)  notation += "#";
        else if (isCheck) notation += "+";

        moveNumber++;
        move.setPiece(board[tr][tc]);
        move.setNotation(notation);
        move.setMoveNumber(moveNumber);

        // save to DB
        try {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            move.setGame(game);
            moveRepository.save(move);
        } catch (Exception e) { e.printStackTrace(); }

        moves.add(move);

        if (isWhite) whiteHistory.add(notation);
        else         blackHistory.add(notation);

        System.out.println("MOVE: "+fr+","+fc+" -> "+tr+","+tc+" | "+notation);

        GameState.switchTurn();

        // determine result
        if (isCheckmate) {
            gameResult = isWhite ? "WHITE_WIN" : "BLACK_WIN";
            gameService.updateGameStatus(gameId, gameResult); // update DB
            System.out.println("CHECKMATE: " + gameResult);
            return gameResult;
        }
        if (isStalemate) {
            gameResult = "STALEMATE";
            gameService.updateGameStatus(gameId, gameResult); // update DB
            System.out.println("STALEMATE");
            return gameResult;
        }

        return "OK";
    }

    // ================= TIMEOUT =================
    public String declareTimeout(Long gameId, String loserColor) {
        gameResult = loserColor.equals("white") ? "BLACK_WIN" : "WHITE_WIN";
        gameService.updateGameStatus(gameId, gameResult); // update DB
        return gameResult;
    }

    // ================= UNDO =================
    public void undo() {
        if (moves.isEmpty()) return;

        Move last = moves.remove(moves.size() - 1);
        moveNumber--;
        gameResult = "ONGOING";

        try {
            if (last.getId() != null) moveRepository.deleteById(last.getId());
        } catch (Exception e) { e.printStackTrace(); }

        if (GameState.currentTurn.equals("white")) {
            if (!blackHistory.isEmpty())  blackHistory.remove(blackHistory.size() - 1);
        } else {
            if (!whiteHistory.isEmpty()) whiteHistory.remove(whiteHistory.size() - 1);
        }

        Board.resetBoard();
        String[][] board = Board.getBoard();
        for (Move m : moves) {
            board[m.getToRow()][m.getToCol()]     = m.getPiece();
            board[m.getFromRow()][m.getFromCol()] = null;
        }
        GameState.switchTurn();
    }

    // ================= RESET =================
    public void reset() {
        resetState();
    }

    // internal reset - called on new user login too
    private void resetState() {
        moves.clear();
        whiteHistory.clear();
        blackHistory.clear();
        moveNumber = 0;
        gameResult = "ONGOING";
        Board.resetBoard();
        GameState.reset();
    }
}