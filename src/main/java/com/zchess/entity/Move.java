package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Move {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private Long gameId;
                    private String moveText;

                        public Long getId() { return id; }
                            public Long getGameId() { return gameId; }
                                public String getMoveText() { return moveText; }

                                    public void setId(Long id) { this.id = id; }
                                        public void setGameId(Long gameId) { this.gameId = gameId; }
                                            public void setMoveText(String moveText) { this.moveText = moveText; }
                                            }