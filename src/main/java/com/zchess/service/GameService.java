package com.zchess.service;

import com.zchess.entity.Game;
import com.zchess.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository repo;
        private final ChessService chess;

            public GameService(GameRepository repo, ChessService chess){
                    this.repo = repo;
                            this.chess = chess;
                                }

                                    // create new game
                                        public Game createGame(){

                                                Game g = new Game();
                                                        g.setBoardState(chess.initialBoard());
                                                                g.setCurrentTurn("white");
                                                                        g.setCheckmate(false);

                                                                                repo.save(g);
                                                                                        return g;
                                                                                            }

                                                                                                // make move
                                                                                                    public Game makeMove(Long id,String from,String to){

                                                                                                            Game g = repo.findById(id).orElseThrow();

                                                                                                                    String newBoard =
                                                                                                                                    chess.tryMove(
                                                                                                                                                            g.getBoardState(),
                                                                                                                                                                                    from,
                                                                                                                                                                                                            to,
                                                                                                                                                                                                                                    g.getCurrentTurn()
                                                                                                                                                                                                                                                    );

                                                                                                                                                                                                                                                            // illegal move → return same game
                                                                                                                                                                                                                                                                    if(newBoard.equals(g.getBoardState()))
                                                                                                                                                                                                                                                                                return g;

                                                                                                                                                                                                                                                                                        g.setBoardState(newBoard);
                                                                                                                                                                                                                                                                                                g.setCurrentTurn(
                                                                                                                                                                                                                                                                                                                g.getCurrentTurn().equals("white")
                                                                                                                                                                                                                                                                                                                                        ? "black" : "white"
                                                                                                                                                                                                                                                                                                                                                );

                                                                                                                                                                                                                                                                                                                                                        boolean mate =
                                                                                                                                                                                                                                                                                                                                                                        chess.isCheckmate(newBoard, g.getCurrentTurn());

                                                                                                                                                                                                                                                                                                                                                                                g.setCheckmate(mate);

                                                                                                                                                                                                                                                                                                                                                                                        repo.save(g);
                                                                                                                                                                                                                                                                                                                                                                                                return g;
                                                                                                                                                                                                                                                                                                                                                                                                    }

                                                                                                                                                                                                                                                                                                                                                                                                        public Game get(Long id){
                                                                                                                                                                                                                                                                                                                                                                                                                return repo.findById(id).orElseThrow();
                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                    }