                                       package com.zchess.service;

                                       import com.zchess.entity.Move;
                                       import com.zchess.engine.Board;
                                       import com.zchess.engine.MoveValidator;
                                       import com.zchess.repository.MoveRepository;

                                       import org.springframework.stereotype.Service;

                                       @Service
                                       public class MoveService {

                                           private final MoveRepository moveRepository;
                                               private final Board board;

                                                   public MoveService(MoveRepository moveRepository) {
                                                           this.moveRepository = moveRepository;
                                                                   this.board = new Board();
                                                                       }

                                                                           public String[][] getBoard(){
                                                                                   return board.getBoard();
                                                                                       }

                                                                                           public String[][] makeMove(Move move){

                                                                                                   int fr = move.getFromRow();
                                                                                                           int fc = move.getFromCol();
                                                                                                                   int tr = move.getToRow();
                                                                                                                           int tc = move.getToCol();

                                                                                                                                   String piece = board.getBoard()[fr][fc];

                                                                                                                                           if(piece == null || piece.equals("")){
                                                                                                                                                       throw new RuntimeException("No piece selected");
                                                                                                                                                               }

                                                                                                                                                                       boolean valid = MoveValidator.isValidMove(
                                                                                                                                                                                       piece,
                                                                                                                                                                                                       fr,
                                                                                                                                                                                                                       fc,
                                                                                                                                                                                                                                       tr,
                                                                                                                                                                                                                                                       tc,
                                                                                                                                                                                                                                                                       board.getBoard()
                                                                                                                                                                                                                                                                               );

                                                                                                                                                                                                                                                                                       if(!valid){
                                                                                                                                                                                                                                                                                                   throw new RuntimeException("Invalid move");
                                                                                                                                                                                                                                                                                                           }

                                                                                                                                                                                                                                                                                                                   board.movePiece(fr,fc,tr,tc);

                                                                                                                                                                                                                                                                                                                           moveRepository.save(move);

                                                                                                                                                                                                                                                                                                                                   return board.getBoard();
                                                                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                                                                       }