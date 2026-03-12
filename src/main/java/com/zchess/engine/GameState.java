package com.zchess.engine;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    public static char turn = 'w';

        private static List<String> moveHistory = new ArrayList<>();

            public static void addMove(String move){
                    moveHistory.add(move);
                        }

                            public static List<String> getHistory(){
                                    return moveHistory;
                                        }
                                        public static void undo(){

                                                if(moveHistory.size() > 0)
                                                        moveHistory.remove(moveHistory.size()-1);

                                                        }
                                        

                                            public static void reset(){
                                                    moveHistory.clear();
                                                            turn = 'w';
                                                                }

                                                                }