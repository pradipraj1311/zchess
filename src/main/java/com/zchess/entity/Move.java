package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Move {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private Long gameId;
                    private String fromPos;
                        private String toPos;
                            private String piece;

                                public Move() {}

                                    public Move(Long gameId, String fromPos, String toPos, String piece) {
                                            this.gameId = gameId;
                                                    this.fromPos = fromPos;
                                                            this.toPos = toPos;
                                                                    this.piece = piece;
                                                                        }
                                                                        }