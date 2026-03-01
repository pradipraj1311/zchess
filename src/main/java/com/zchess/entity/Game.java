package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String board;

                    private String currentTurn;

                        public Game() {}

                            public Long getId() { return id; }

                                public String getBoard() { return board; }
                                    public void setBoard(String board) { this.board = board; }

                                        public String getCurrentTurn() { return currentTurn; }
                                            public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }
                                            }