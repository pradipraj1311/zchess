package com.zchess.engine;

public class MoveValidator {

    public static boolean isValidMove(String piece,
                                          int fr,
                                                                                int fc,
                                                                                                                      int tr,
                                                                                                                                                            int tc,
                                                                                                                                                                                                  String[][] board) {

                                                                                                                                                                                                          if (piece == null || piece.equals("")) return false;

                                                                                                                                                                                                                  char type = piece.charAt(1);

                                                                                                                                                                                                                          switch (type) {

                                                                                                                                                                                                                                      case 'p':
                                                                                                                                                                                                                                                      return PawnValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                                  case 'r':
                                                                                                                                                                                                                                                                                  return RookValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                                                              case 'n':
                                                                                                                                                                                                                                                                                                              return KnightValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                                                                                          case 'b':
                                                                                                                                                                                                                                                                                                                                          return BishopValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                                                                                                                      case 'q':
                                                                                                                                                                                                                                                                                                                                                                      return QueenValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                                                                                                                                                  case 'k':
                                                                                                                                                                                                                                                                                                                                                                                                  return KingValidator.isValid(board, fr, fc, tr, tc);

                                                                                                                                                                                                                                                                                                                                                                                                              default:
                                                                                                                                                                                                                                                                                                                                                                                                                              return false;
                                                                                                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                                                                                                                          }