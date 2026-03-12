package com.zchess.engine;

public class ChessNotation {

    public static String convert(String piece, int fr, int fc, int tr, int tc){

            char type = piece.charAt(1);

                    String pieceLetter = "";

                            switch(type){

                                        case 'p': pieceLetter=""; break;
                                                    case 'n': pieceLetter="N"; break;
                                                                case 'b': pieceLetter="B"; break;
                                                                            case 'r': pieceLetter="R"; break;
                                                                                        case 'q': pieceLetter="Q"; break;
                                                                                                    case 'k': pieceLetter="K"; break;

                                                                                                            }

                                                                                                                    char file = (char) ('a' + tc);
                                                                                                                            int rank = 8 - tr;

                                                                                                                                    return pieceLetter + file + rank;
                                                                                                                                        }
                                                                                                                                        }