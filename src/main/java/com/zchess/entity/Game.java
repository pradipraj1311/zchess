package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String playerWhite;
                    private String playerBlack;

                        private String status;

                            public Game() {
                                }

                                    public Game(String playerWhite, String playerBlack, String status) {
                                            this.playerWhite = playerWhite;
                                                    this.playerBlack = playerBlack;
                                                            this.status = status;
                                                                }

                                                                    public Long getId() {
                                                                            return id;
                                                                                }

                                                                                    public String getPlayerWhite() {
                                                                                            return playerWhite;
                                                                                                }

                                                                                                    public void setPlayerWhite(String playerWhite) {
                                                                                                            this.playerWhite = playerWhite;
                                                                                                                }

                                                                                                                    public String getPlayerBlack() {
                                                                                                                            return playerBlack;
                                                                                                                                }

                                                                                                                                    public void setPlayerBlack(String playerBlack) {
                                                                                                                                            this.playerBlack = playerBlack;
                                                                                                                                                }

                                                                                                                                                    public String getStatus() {
                                                                                                                                                            return status;
                                                                                                                                                                }

                                                                                                                                                                    public void setStatus(String status) {
                                                                                                                                                                            this.status = status;
                                                                                                                                                                                }
                                                                                                                                                                                }