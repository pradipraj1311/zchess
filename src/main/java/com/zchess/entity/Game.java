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

                            @Column(length = 4000)
                                private String moveHistory;

                                    private boolean checkmate;

                                        public Long getId() { return id; }
                                            public String getBoardState() { return boardState; }
                                                public String getCurrentTurn() { return currentTurn; }
                                                    public String getMoveHistory() { return moveHistory; }
                                                        public boolean isCheckmate() { return checkmate; }

                                                            public void setId(Long id) { this.id = id; }
                                                                public void setBoardState(String boardState) { this.boardState = boardState; }
                                                                    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }
                                                                        public void setMoveHistory(String moveHistory) { this.moveHistory = moveHistory; }
                                                                            public void setCheckmate(boolean checkmate) { this.checkmate = checkmate; }
                                                                            }