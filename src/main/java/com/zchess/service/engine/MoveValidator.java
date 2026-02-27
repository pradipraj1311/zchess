package com.zchess.service.engine;

import com.zchess.entity.*;

public class MoveValidator {

    public boolean isValid(Game game, Piece piece, int target) {

            int from = piece.getPosition();
                    int dx = (target % 8) - (from % 8);
                            int dy = (target / 8) - (from / 8);

                                    switch (piece.getType()) {

                                                case PAWN:
                                                                return validatePawn(piece, dx, dy);

                                                                            case ROOK:
                                                                                            return dx == 0 || dy == 0;

                                                                                                        case BISHOP:
                                                                                                                        return Math.abs(dx) == Math.abs(dy);

                                                                                                                                    case QUEEN:
                                                                                                                                                    return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);

                                                                                                                                                                case KNIGHT:
                                                                                                                                                                                return (Math.abs(dx) == 2 && Math.abs(dy) == 1)
                                                                                                                                                                                                        || (Math.abs(dx) == 1 && Math.abs(dy) == 2);

                                                                                                                                                                                                                    case KING:
                                                                                                                                                                                                                                    return Math.abs(dx) <= 1 && Math.abs(dy) <= 1;
                                                                                                                                                                                                                                            }

                                                                                                                                                                                                                                                    return false;
                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                            private boolean validatePawn(Piece pawn, int dx, int dy) {

                                                                                                                                                                                                                                                                    int direction = pawn.getColor() == Color.WHITE ? -1 : 1;

                                                                                                                                                                                                                                                                            if (dx == 0 && dy == direction)
                                                                                                                                                                                                                                                                                        return true;

                                                                                                                                                                                                                                                                                                if (!pawn.isMoved() && dx == 0 && dy == 2 * direction)
                                                                                                                                                                                                                                                                                                            return true;

                                                                                                                                                                                                                                                                                                                    return false;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        }