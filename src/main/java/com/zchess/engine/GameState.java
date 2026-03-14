package com.zchess.engine;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    public static String currentTurn = "white";

        private static List<String> history=new ArrayList<>();

            private static List<String[][]> boardHistory=new ArrayList<>();

                public static void addMove(String move,String[][] board){

                        history.add(move);

                                String[][] copy=new String[8][8];

                                        for(int r=0;r<8;r++)
                                                    for(int c=0;c<8;c++)
                                                                    copy[r][c]=board[r][c];

                                                                            boardHistory.add(copy);

                                                                                }

                                                                                    public static List<String> getHistory(){
                                                                                            return history;
                                                                                                }

                                                                                                    public static void undo(){

                                                                                                            if(boardHistory.size()==0)
                                                                                                                        return;

                                                                                                                                String[][] last=boardHistory.remove(boardHistory.size()-1);

                                                                                                                                        for(int r=0;r<8;r++)
                                                                                                                                                    for(int c=0;c<8;c++)
                                                                                                                                                                    Board.getBoard()[r][c]=last[r][c];

                                                                                                                                                                            if(history.size()>0)
                                                                                                                                                                                        history.remove(history.size()-1);

                                                                                                                                                                                                currentTurn = currentTurn.equals("white") ? "black" : "white";

                                                                                                                                                                                                    }

                                                                                                                                                                                                        public static void reset(){

                                                                                                                                                                                                                history.clear();
                                                                                                                                                                                                                        boardHistory.clear();
                                                                                                                                                                                                                                currentTurn = "white";
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                