package com.zchess.engine;

public class PawnValidator {

    public static boolean isValid(int fr, int fc, int tr, int tc) {

            int direction = tr - fr;

                    // pawn forward move
                            if (fc == tc && direction == 1) {
                                        return true;
                                                }

                                                        // pawn diagonal capture
                                                                if (Math.abs(fc - tc) == 1 && direction == 1) {
                                                                            return true;
                                                                                    }

                                                                                            return false;
                                                                                                }
                                                                                                }