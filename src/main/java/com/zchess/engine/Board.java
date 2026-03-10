package com.zchess.engine;

public class Board {

    private static String[][] board = {

                {"br","bn","bb","bq","bk","bb","bn","br"},
                            {"bp","bp","bp","bp","bp","bp","bp","bp"},
                                        {"","","","","","","",""},
                                                    {"","","","","","","",""},
                                                                {"","","","","","","",""},
                                                                            {"","","","","","","",""},
                                                                                        {"wp","wp","wp","wp","wp","wp","wp","wp"},
                                                                                                    {"wr","wn","wb","wq","wk","wb","wn","wr"}

                                                                                                        };

                                                                                                            public static String[][] getBoard() {
                                                                                                                    return board;
                                                                                                                        }

                                                                                                                            public static void move(int fr,int fc,int tr,int tc){

                                                                                                                                    String piece = board[fr][fc];

                                                                                                                                            board[tr][tc] = piece;
                                                                                                                                                    board[fr][fc] = "";

                                                                                                                                                        }
                                                                                                                                                        }