package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String currentTurn;

                    @Column(length = 500)
                        private String boardState;

                            // ===== GETTERS =====

                                public Long getId() {
                                        return id;
                                            }

                                                public String getCurrentTurn() {
                                                        return currentTurn;
                                                            }

                                                                public String getBoardState() {
                                                                        return boardState;
                                                                            }

                                                                                // ===== SETTERS =====

                                                                                    public void setCurrentTurn(String currentTurn) {
                                                                                            this.currentTurn = currentTurn;
                                                                                                }

                                                                                                    public void setBoardState(String boardState) {
                                                                                                            this.boardState = boardState;
                                                                                                                }
                                                                                                                }