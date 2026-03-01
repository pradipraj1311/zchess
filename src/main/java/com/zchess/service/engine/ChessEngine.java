package com.zchess.service.engine;

public class ChessEngine {

    private Board board;
        private String currentTurn = "WHITE";

            public ChessEngine() {
                    board = new Board();
                        }

                            public String getBoard() {
                                    return board.serialize();
                                        }

                                            public String getCurrentTurn() {
                                                    return currentTurn;
                                                        }

                                                            public void makeMove(Move move) {

                                                                    if(!MoveValidator.isValidMove(move, board, currentTurn))
                                                                                return;

                                                                                        board.movePiece(move.getFrom(), move.getTo());

                                                                                                switchTurn();
                                                                                                    }

                                                                                                        private void switchTurn() {
                                                                                                                currentTurn = currentTurn.equals("WHITE") ? "BLACK" : "WHITE";
                                                                                                                    }
                                                                                                                    }