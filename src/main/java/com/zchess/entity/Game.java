package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                @Column(length = 2000)
                    private String boardState;

                        private String currentTurn;

                            @Column(length = 5000)
                                private String moveHistory;

                                    private boolean checkmate;

                                        public Game(){}

                                            public Long getId(){ return id; }
                                                public void setId(Long id){ this.id=id; }

                                                    public String getBoardState(){ return boardState; }
                                                        public void setBoardState(String boardState){ this.boardState=boardState; }

                                                            public String getCurrentTurn(){ return currentTurn; }
                                                                public void setCurrentTurn(String currentTurn){ this.currentTurn=currentTurn; }

                                                                    public String getMoveHistory(){ return moveHistory; }
                                                                        public void setMoveHistory(String moveHistory){ this.moveHistory=moveHistory; }

                                                                            public boolean isCheckmate(){ return checkmate; }
                                                                                public void setCheckmate(boolean checkmate){ this.checkmate=checkmate; }
                                                                                }