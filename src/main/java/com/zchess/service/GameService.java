
package com.zchess.service;

import org.springframework.stereotype.Service;

import com.zchess.engine.Board;

@Service
public class GameService {

    public String[][] getBoard(){

            return Board.getBoard();

                }

                }