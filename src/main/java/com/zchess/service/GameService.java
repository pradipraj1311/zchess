
package com.zchess.service;

import com.zchess.engine.Board;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public String[][] getBoard(){

            return Board.getBoard();

                }

                }