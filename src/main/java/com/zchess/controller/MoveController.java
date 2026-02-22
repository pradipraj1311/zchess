package com.zchess.controller;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import com.zchess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/move")
public class MoveController {

    @Autowired
        GameRepository gameRepo;

            @Autowired
                ChessService chessService;

                    @PostMapping("/{id}")
                        public Game move(
                                    @PathVariable Long id,
                                                @RequestParam int from,
                                                            @RequestParam int to
                                                                ) {

                                                                        Game g = gameRepo.findById(id).orElseThrow();

                                                                                String[] board = g.getBoardState().split(",");

                                                                                        if (!chessService.isValidMove(board, from, to, g.getCurrentTurn()))
                                                                                                    return g;

                                                                                                            String piece = board[from];
                                                                                                                    board[from] = ".";
                                                                                                                            board[to] = piece;

                                                                                                                                    String turn = g.getCurrentTurn().equals("white") ? "black" : "white";

                                                                                                                                            if (chessService.isKingInCheck(board, turn)) {
                                                                                                                                                        System.out.println("King in check!");
                                                                                                                                                                }

                                                                                                                                                                        g.setCurrentTurn(turn);
                                                                                                                                                                                g.setBoardState(String.join(",", board));

                                                                                                                                                                                        gameRepo.save(g);
                                                                                                                                                                                                return g;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    }