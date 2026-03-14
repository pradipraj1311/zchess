package com.zchess.engine;

public class ChessNotation {

    public static String convert(String piece, int fr, int fc, int tr, int tc, boolean capture){

            char file = (char) ('a' + tc);
                    int rank = 8 - tr;

                            String square = "" + file + rank;

                                    // Pawn moves
                                            if(piece.equals("wp") || piece.equals("bp")){

                                                        if(capture){
                                                                        char fromFile = (char) ('a' + fc);
                                                                                        return fromFile + "x" + square;
                                                                                                    }

                                                                                                                return square;
                                                                                                                        }

                                                                                                                                String letter="";

                                                                                                                                        switch(piece.charAt(1)){
                                                                                                                                                    case 'r': letter="R"; break;
                                                                                                                                                                case 'n': letter="N"; break;
                                                                                                                                                                            case 'b': letter="B"; break;
                                                                                                                                                                                        case 'q': letter="Q"; break;
                                                                                                                                                                                                    case 'k': letter="K"; break;
                                                                                                                                                                                                            }

                                                                                                                                                                                                                    if(capture)
                                                                                                                                                                                                                                return letter + "x" + square;

                                                                                                                                                                                                                                        return letter + square;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            }