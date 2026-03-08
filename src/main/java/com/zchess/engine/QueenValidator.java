package com.zchess.engine;

public class QueenValidator {

    public static boolean isValid(String[][] board,int fr,int fc,int tr,int tc){

            if(fr == tr || fc == tc){
                        return RookValidator.isValid(board,fr,fc,tr,tc);
                                }

                                        if(Math.abs(fr-tr) == Math.abs(fc-tc)){
                                                    return BishopValidator.isValid(board,fr,fc,tr,tc);
                                                            }

                                                                    return false;
                                                                        }
                                                                        }