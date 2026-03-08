package com.zchess.engine;

public class PawnValidator {

    public static boolean isValid(String[][] board,int fr,int fc,int tr,int tc){

            String piece = board[fr][fc];

                    int direction = piece.charAt(0) == 'w' ? -1 : 1;

                            // forward move
                                    if(fc == tc && board[tr][tc].equals("")){
                                                if(tr - fr == direction){
                                                                return true;
                                                                            }
                                                                                    }

                                                                                            // capture
                                                                                                    if(Math.abs(fc - tc) == 1 && tr - fr == direction){
                                                                                                                if(!board[tr][tc].equals("")){
                                                                                                                                return true;
                                                                                                                                            }
                                                                                                                                                    }

                                                                                                                                                            return false;
                                                                                                                                                                }
                                                                                                                                                                }