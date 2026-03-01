package com.zchess.service.engine;

public class Board {

    private String[][] grid = new String[8][8];

        public Board() {
                initialize();
                    }

                        private void initialize() {

                                grid[0] = new String[]{"br","bn","bb","bq","bk","bb","bn","br"};
                                        grid[1] = new String[]{"bp","bp","bp","bp","bp","bp","bp","bp"};

                                                for(int i=2;i<6;i++)
                                                            for(int j=0;j<8;j++)
                                                                            grid[i][j] = ".";

                                                                                    grid[6] = new String[]{"wp","wp","wp","wp","wp","wp","wp","wp"};
                                                                                            grid[7] = new String[]{"wr","wn","wb","wq","wk","wb","wn","wr"};
                                                                                                }

                                                                                                    public String getPiece(int row, int col) {
                                                                                                            return grid[row][col];
                                                                                                                }

                                                                                                                    public void setPiece(int row, int col, String value) {
                                                                                                                            grid[row][col] = value;
                                                                                                                                }

                                                                                                                                    public void movePiece(Position from, Position to) {

                                                                                                                                            String piece = grid[from.getRow()][from.getCol()];

                                                                                                                                                    grid[to.getRow()][to.getCol()] = piece;
                                                                                                                                                            grid[from.getRow()][from.getCol()] = ".";
                                                                                                                                                                }

                                                                                                                                                                    public boolean isInside(int row, int col) {
                                                                                                                                                                            return row >= 0 && row < 8 && col >= 0 && col < 8;
                                                                                                                                                                                }

                                                                                                                                                                                    public String[][] getGrid() {
                                                                                                                                                                                            return grid;
                                                                                                                                                                                                }

                                                                                                                                                                                                    public String serialize() {

                                                                                                                                                                                                            StringBuilder sb = new StringBuilder();

                                                                                                                                                                                                                    for(int i=0;i<8;i++) {
                                                                                                                                                                                                                                for(int j=0;j<8;j++) {
                                                                                                                                                                                                                                                sb.append(grid[i][j]);
                                                                                                                                                                                                                                                                if(!(i==7 && j==7))
                                                                                                                                                                                                                                                                                    sb.append(",");
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                return sb.toString();
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                    }