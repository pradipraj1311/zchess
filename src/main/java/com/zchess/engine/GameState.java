package com.zchess.engine;

public class GameState {

    private static String turn = "white";

        public static String getTurn(){
                return turn;
                    }

                        public static void switchTurn(){

                                if(turn.equals("white")){
                                            turn="black";
                                                    }else{
                                                                turn="white";
                                                                        }

                                                                            }

                                                                            }