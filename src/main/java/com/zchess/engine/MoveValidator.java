package com.zchess.engine;

public class MoveValidator {

    public static boolean isValid(String piece, int fr, int fc, int tr, int tc) {

            if (piece == null || piece.isEmpty()) {
                        return false;
                                }

                                        char type = Character.toLowerCase(piece.charAt(1));

                                                switch (type) {

                                                            case 'p':
                                                                            return PawnValidator.isValid(fr, fc, tr, tc);

                                                                                        case 'r':
                                                                                                        return RookValidator.isValid(fr, fc, tr, tc);

                                                                                                                    case 'n':
                                                                                                                                    return KnightValidator.isValid(fr, fc, tr, tc);

                                                                                                                                                case 'b':
                                                                                                                                                                return BishopValidator.isValid(fr, fc, tr, tc);

                                                                                                                                                                            case 'q':
                                                                                                                                                                                            return QueenValidator.isValid(fr, fc, tr, tc);

                                                                                                                                                                                                        case 'k':
                                                                                                                                                                                                                        return KingValidator.isValid(fr, fc, tr, tc);

                                                                                                                                                                                                                                    default:
                                                                                                                                                                                                                                                    return false;
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                