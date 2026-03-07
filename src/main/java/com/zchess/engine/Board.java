package com.zchess.engine;

public class Board {

    private String[][] board = new String[8][8];

        public Board() {
                initialize();
                    }

                        private void initialize() {

                                board[0] = new String[]{"br","bn","bb","bq","bk","bb","bn","br"};
                                        board[1] = new String[]{"bp","bp","bp","bp","bp","bp","bp","bp"};

                                                for(int i=2;i<6;i++){
                                                            for(int j=0;j<8;j++){
                                                                            board[i][j] = "";
                                                                                        }
                                                                                                }

                                                                                                        board[6] = new String[]{"wp","wp","wp","wp","wp","wp","wp","wp"};
                                                                                                                board[7] = new String[]{"wr","wn","wb","wq","wk","wb","wn","wr"};
                                                                                                                    }

                                                                                                                        public String getPiece(int r,int c){
                                                                                                                                return board[r][c];
                                                                                                                                    }

                                                                                                                                        public void movePiece(int fr,int fc,int tr,int tc){
                                                                                                                                                board[tr][tc] = board[fr][fc];
                                                                                                                                                        board[fr][fc] = "";
                                                                                                                                                            }

                                                                                                                                                                public String[][] getBoard(){
                                                                                                                                                                        return board;
                                                                                                                                                                            }
                                                                                                                                                                            }
