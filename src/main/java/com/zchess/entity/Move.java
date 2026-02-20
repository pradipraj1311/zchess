package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Move {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private int moveNumber;
                    private String playerColor;
                        private String moveNotation;

                            @ManyToOne
                                @JoinColumn(name="game_id")
                                    private Game game;

                                        public Long getId() { return id; }

                                            public int getMoveNumber() { return moveNumber; }
                                                public void setMoveNumber(int moveNumber) { this.moveNumber = moveNumber; }

                                                    public String getPlayerColor() { return playerColor; }
                                                        public void setPlayerColor(String playerColor) { this.playerColor = playerColor; }

                                                            public String getMoveNotation() { return moveNotation; }
                                                                public void setMoveNotation(String moveNotation) { this.moveNotation = moveNotation; }

                                                                    public Game getGame() { return game; }
                                                                        public void setGame(Game game) { this.game = game; }
                                                                        }