package com.zchess.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String status; // ACTIVE / FINISHED
                    private String currentTurn; // WHITE / BLACK
                        private LocalDateTime createdAt;

                            @ManyToOne
                                private User user;

                                    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
                                        private List<Move> moves;

                                            public Game(){
                                                    this.createdAt = LocalDateTime.now();
                                                            this.status = "ACTIVE";
                                                                    this.currentTurn = "WHITE";
                                                                        }

                                                                            public Long getId(){ return id; }

                                                                                public String getStatus(){ return status; }
                                                                                    public void setStatus(String status){ this.status = status; }

                                                                                        public String getCurrentTurn(){ return currentTurn; }
                                                                                            public void setCurrentTurn(String currentTurn){ this.currentTurn = currentTurn; }

                                                                                                public LocalDateTime getCreatedAt(){ return createdAt; }

                                                                                                    public User getUser(){ return user; }
                                                                                                        public void setUser(User user){ this.user = user; }

                                                                                                            public List<Move> getMoves(){ return moves; }
                                                                                                                public void setMoves(List<Move> moves){ this.moves = moves; }
                                                                                                                }