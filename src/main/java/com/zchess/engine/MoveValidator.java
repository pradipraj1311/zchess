package com.zchess.engine;

public class MoveValidator {

    public static boolean isValid(String[][] board, int fr, int fc, int tr, int tc){

            String piece = board[fr][fc];

                    if(piece == null || piece.equals("")){
                                return false;
                                        }

                                                String target = board[tr][tc];

                                                        // prevent capturing own piece
                                                                if(target != null && !target.equals("")){
                                                                            if(target.charAt(0) == piece.charAt(0)){
                                                                                            return false;
                                                                                                        }
                                                                                                                }

                                                                                                                        char type = piece.charAt(1);

                                                                                                                                switch(type){

                                                                                                                                            case 'p':
                                                                                                                                                            return PawnValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                        case 'r':
                                                                                                                                                                                        return RookValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                    case 'n':
                                                                                                                                                                                                                    return KnightValidator.isValid(fr, fc, tr, tc);

                                                                                                                                                                                                                                case 'b':
                                                                                                                                                                                                                                                return BishopValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                            case 'q':
                                                                                                                                                                                                                                                                            return QueenValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                                                        case 'k':
                                                                                                                                                                                                                                                                                                        return KingValidator.isValid(fr, fc, tr, tc);
                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                        return false;
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            }