package com.zchess.service.engine;

import com.zchess.entity.*;
import java.util.*;

public class ChessEngine {

    private final MoveValidator validator = new MoveValidator();
        private final CheckDetector checkDetector = new CheckDetector();
            private final CheckmateDetector mateDetector = new CheckmateDetector();
                private final NotationGenerator notationGenerator = new NotationGenerator();

                    public void executeMove(Game game, int from, int to) {

                            Piece piece = findPiece(game, from);

                                    if (piece == null)
                                                throw new RuntimeException("No piece on square");

                                                        if (piece.getColor() != game.getCurrentTurn())
                                                                    throw new RuntimeException("Wrong turn");

                                                                            if (!validator.isValid(game, piece, to))
                                                                                        throw new RuntimeException("Illegal move");

                                                                                                String notation = notationGenerator.generate(game, piece, to);

                                                                                                        piece.setPosition(to);
                                                                                                                piece.setMoved(true);

                                                                                                                        game.getMoves().add(new Move(from, to, notation, piece.getColor()));

                                                                                                                                boolean check = checkDetector.isCheck(game, piece.getColor().opposite());
                                                                                                                                        boolean mate = false;

                                                                                                                                                if (check)
                                                                                                                                                            mate = mateDetector.isCheckmate(game, piece.getColor().opposite());

                                                                                                                                                                    game.setCheck(check);
                                                                                                                                                                            game.setCheckmate(mate);
                                                                                                                                                                                    game.setCurrentTurn(piece.getColor().opposite());
                                                                                                                                                                                        }

                                                                                                                                                                                            private Piece findPiece(Game game, int position) {
                                                                                                                                                                                                    return game.getPieces()
                                                                                                                                                                                                                    .stream()
                                                                                                                                                                                                                                    .filter(p -> p.getPosition() == position)
                                                                                                                                                                                                                                                    .findFirst()
                                                                                                                                                                                                                                                                    .orElse(null);
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        }