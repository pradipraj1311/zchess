package com.zchess.engine;


public class GameState {

    // keeps track of current turn
    public static String currentTurn = "white";

    // switch turn after every valid move
    public static void switchTurn() {
        currentTurn = currentTurn.equals("white") ? "black" : "white";
    }

    // reset game state
    public static void reset() {
        currentTurn = "white";
    }
}