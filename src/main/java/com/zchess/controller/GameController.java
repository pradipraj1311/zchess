package com.zchess.controller;

import org.springframework.web.bind.annotation.*;
import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;

@RestController
@RequestMapping("/api/game")
@CrossOrigin
public class GameController {

    private final GameRepository repo;

        public GameController(GameRepository repo){
                this.repo = repo;
                    }

                        // CREATE GAME
                            @GetMapping("/create")
                                public Game createGame(){

                                        Game g = new Game();

                                                g.setCurrentTurn("white");
                                                        g.setBoardState(
                                                                    "bR,bN,bB,bQ,bK,bB,bN,bR;" +
                                                                                "bP,bP,bP,bP,bP,bP,bP,bP;" +
                                                                                            ".,.,.,.,.,.,.,.;" +
                                                                                                        ".,.,.,.,.,.,.,.;" +
                                                                                                                    ".,.,.,.,.,.,.,.;" +
                                                                                                                                ".,.,.,.,.,.,.,.;" +
                                                                                                                                            "wP,wP,wP,wP,wP,wP,wP,wP;" +
                                                                                                                                                        "wR,wN,wB,wQ,wK,wB,wN,wR"
                                                                                                                                                                );

                                                                                                                                                                        return repo.save(g);
                                                                                                                                                                            }

                                                                                                                                                                                // GET GAME
                                                                                                                                                                                    @GetMapping("/{id}")
                                                                                                                                                                                        public Game getGame(@PathVariable Long id){
                                                                                                                                                                                                return repo.findById(id).orElse(null);
                                                                                                                                                                                                    }

                                                                                                                                                                                                        // MOVE
                                                                                                                                                                                                            @PostMapping("/move")
                                                                                                                                                                                                                public Game move(
                                                                                                                                                                                                                        @RequestParam int fromRow,
                                                                                                                                                                                                                                @RequestParam int fromCol,
                                                                                                                                                                                                                                        @RequestParam int toRow,
                                                                                                                                                                                                                                                @RequestParam int toCol
                                                                                                                                                                                                                                                    ){

                                                                                                                                                                                                                                                            Game g = repo.findById(1L).orElse(null);

                                                                                                                                                                                                                                                                    String[] rows = g.getBoardState().split(";");
                                                                                                                                                                                                                                                                            String[][] board = new String[8][8];

                                                                                                                                                                                                                                                                                    for(int i=0;i<8;i++){
                                                                                                                                                                                                                                                                                                board[i] = rows[i].split(",");
                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                String piece = board[fromRow][fromCol];
                                                                                                                                                                                                                                                                                                                        board[fromRow][fromCol] = ".";
                                                                                                                                                                                                                                                                                                                                board[toRow][toCol] = piece;

                                                                                                                                                                                                                                                                                                                                        String newBoard = "";
                                                                                                                                                                                                                                                                                                                                                for(int i=0;i<8;i++){
                                                                                                                                                                                                                                                                                                                                                            newBoard += String.join(",", board[i]);
                                                                                                                                                                                                                                                                                                                                                                        if(i<7) newBoard += ";";
                                                                                                                                                                                                                                                                                                                                                                                }

                                                                                                                                                                                                                                                                                                                                                                                        g.setBoardState(newBoard);
                                                                                                                                                                                                                                                                                                                                                                                                return repo.save(g);
                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                    }