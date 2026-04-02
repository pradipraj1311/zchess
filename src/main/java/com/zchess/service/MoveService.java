package com.zchess.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zchess.engine.Board;
import com.zchess.engine.CheckValidator;
import com.zchess.engine.CheckmateDetector;
import com.zchess.engine.ChessNotation;
import com.zchess.engine.GameState;
import com.zchess.engine.KingValidator;
import com.zchess.engine.MoveValidator;
import com.zchess.engine.PawnValidator;
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
    private static Long         activeGameId  = null;

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

    // Called by GameController when new game is created
    public void setActiveGame(Long gameId) {
        activeGameId = gameId;
        System.out.println("Active game set: " + gameId);
    }

    // ================= MOVE =================
    public String move(Long gameId, Move move) {

        // Different game requested - reset
        if (activeGameId == null || !activeGameId.equals(gameId)) {
            System.out.println("Switching game: " + activeGameId + " -> " + gameId);
            resetState();
            activeGameId = gameId;
        }

        if (!gameResult.equals("ONGOING")) return gameResult;

        // Verify game in DB
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            System.err.println("Game " + gameId + " not found in DB!");
            return "INVALID_NO_PIECE";
        }

        String[][] board = Board.getBoard();
        int fr = move.getFromRow(), fc = move.getFromCol();
        int tr = move.getToRow(),   tc = move.getToCol();

        if (fr < 0||fr > 7||fc < 0||fc > 7||tr < 0||tr > 7||tc < 0||tc > 7) return "INVALID_ILLEGAL";

        String piece = board[fr][fc];
        if (piece == null) return "INVALID_NO_PIECE";

        boolean isWhite = piece.startsWith("w");
        if (isWhite  && !GameState.currentTurn.equals("white")) return "INVALID_WRONG_TURN";
        if (!isWhite && !GameState.currentTurn.equals("black")) return "INVALID_WRONG_TURN";

        String target = board[tr][tc];
        if (target != null && target.startsWith(piece.substring(0,1))) return "INVALID_OWN_PIECE";

        try {
            if (!MoveValidator.isValidMove(piece, fr, fc, tr, tc, board)) return "INVALID_ILLEGAL";
        } catch (Exception e) { return "INVALID_ILLEGAL"; }

        boolean isCastling  = piece.charAt(1) == 'k' && Math.abs(fc-tc) == 2;
        boolean isEnPassant = piece.charAt(1) == 'p' && Math.abs(fc-tc) == 1 && board[tr][tc] == null;

        String capturedEP = null;
        if (isEnPassant) {
            int dir = isWhite ? -1 : 1;
            capturedEP = board[tr-dir][tc];
            board[tr-dir][tc] = null;
        }

        board[tr][tc] = piece;
        board[fr][fc] = null;
        if (isCastling) KingValidator.applyCastling(board, isWhite, fr, fc, tc);

        boolean inCheck = false;
        try { inCheck = CheckValidator.isKingInCheck(board, isWhite); }
        catch (Exception e) { inCheck = true; }

        if (inCheck) {
            board[fr][fc] = piece; board[tr][tc] = target;
            if (isEnPassant && capturedEP != null) { int dir=isWhite?-1:1; board[tr-dir][tc]=capturedEP; }
            if (isCastling) {
                boolean ks = tc > fc;
                if (ks) { board[fr][7]=board[fr][5]; board[fr][5]=null; }
                else    { board[fr][0]=board[fr][3]; board[fr][3]=null; }
            }
            return "INVALID_KING_IN_CHECK";
        }

        if (piece.equals("wp") && tr==0) board[tr][tc]="wq";
        if (piece.equals("bp") && tr==7) board[tr][tc]="bq";

        KingValidator.updateMoveFlags(piece, fr, fc);
        if (piece.charAt(1)=='p') PawnValidator.updateEnPassant(piece,fr,fc,tr,tc);
        else PawnValidator.enPassantTarget = null;

        boolean isCapture = (target != null) || isEnPassant;
        String notation = isCastling ? (tc>fc?"O-O":"O-O-O")
                        : ChessNotation.convert(piece, fr, fc, tr, tc, isCapture);

        boolean opponentIsWhite = !isWhite;
        boolean isCheckmate=false, isStalemate=false, isOppCheck=false;
        try {
            isCheckmate  = CheckmateDetector.isCheckmate(board, opponentIsWhite);
            isStalemate  = CheckmateDetector.isStalemate(board, opponentIsWhite);
            isOppCheck   = CheckValidator.isKingInCheck(board, opponentIsWhite);
        } catch (Exception e) { e.printStackTrace(); }

        if (isCheckmate)      notation += "#";
        else if (isOppCheck)  notation += "+";

        moveNumber++;
        move.setPiece(board[tr][tc]);
        move.setNotation(notation);
        move.setMoveNumber(moveNumber);
        move.setGame(game);

        // SAVE MOVE TO DB
        try {
            Move saved = moveRepository.save(move);
            moves.add(saved);
            System.out.println("Move #" + moveNumber + " saved: " + notation + " game=" + gameId);
        } catch (Exception e) {
            System.err.println("DB save failed: " + e.getMessage());
            e.printStackTrace();
            moves.add(move);
        }

        if (isWhite) whiteHistory.add(notation);
        else         blackHistory.add(notation);

        GameState.switchTurn();

        if (isCheckmate) {
            gameResult = isWhite ? "WHITE_WIN" : "BLACK_WIN";
            gameService.updateGameStatus(gameId, gameResult);
            return gameResult;
        }
        if (isStalemate) {
            gameResult = "STALEMATE";
            gameService.updateGameStatus(gameId, gameResult);
            return gameResult;
        }
        return "OK";
    }

    // ================= TIMEOUT =================
    public String declareTimeout(Long gameId, String loserColor) {
        gameResult = loserColor.equals("white") ? "BLACK_WIN" : "WHITE_WIN";
        gameService.updateGameStatus(gameId, gameResult);
        return gameResult;
    }

    // ================= UNDO =================
    public void undo() {
        if (moves.isEmpty()) return;
        Move last = moves.remove(moves.size()-1);
        moveNumber--;
        gameResult = "ONGOING";

        try {
            if (last.getId() != null) moveRepository.deleteById(last.getId());
        } catch (Exception e) { e.printStackTrace(); }

        if (GameState.currentTurn.equals("white")) {
            if (!blackHistory.isEmpty()) blackHistory.remove(blackHistory.size()-1);
        } else {
            if (!whiteHistory.isEmpty()) whiteHistory.remove(whiteHistory.size()-1);
        }

        Board.resetBoard();
        KingValidator.reset();
        PawnValidator.enPassantTarget = null;
        String[][] board = Board.getBoard();
        for (Move m : moves) {
            board[m.getToRow()][m.getToCol()] = m.getPiece();
            board[m.getFromRow()][m.getFromCol()] = null;
        }
        GameState.switchTurn();
    }

    // ================= RESET =================
    public void reset() { resetState(); }

    private void resetState() {
        moves.clear();
        whiteHistory.clear();
        blackHistory.clear();
        moveNumber = 0;
        gameResult = "ONGOING";
        activeGameId = null;
        Board.resetBoard();
        GameState.reset();
        KingValidator.reset();
        PawnValidator.enPassantTarget = null;
        System.out.println("Board state reset");
    }
}