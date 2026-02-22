package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String boardState;

                    private String currentTurn;

                        public Game() {}

                            public Long getId() {
                                    return id;
                                        }

                                            public String getBoardState() {
                                                    return boardState;
                                                        }

                                                            public void setBoardState(String boardState) {
                                                                    this.boardState = boardState;
                                                                        }

                                                                            public String getCurrentTurn() {
                                                                                    return currentTurn;
                                                                                        }

                                                                                            public void setCurrentTurn(String currentTurn) {
                                                                                                    this.currentTurn = currentTurn;
                                                                                                        }
                                                                                                        }