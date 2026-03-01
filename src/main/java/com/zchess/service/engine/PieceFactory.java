package com.zchess.service.engine;

public class PieceFactory {

    public static Piece createPiece(String code) {

            if(code.equals(".")) return null;

                    String color = code.startsWith("w") ? "WHITE" : "BLACK";
                            char type = code.charAt(1);

                                    switch(type) {
                                                case 'p': return new Pawn(color);
                                                            case 'r': return new Rook(color);
                                                                        case 'n': return new Knight(color);
                                                                                    case 'b': return new Bishop(color);
                                                                                                case 'q': return new Queen(color);
                                                                                                            case 'k': return new King(color);
                                                                                                                    }

                                                                                                                            return null;
                                                                                                                                }
                                                                                                                                }