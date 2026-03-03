package com.zchess.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String status; // ONGOING, FINISHED

                    private String currentTurn;

                        private LocalDateTime createdAt;

                            @ManyToOne
                                @JoinColumn(name = "user_id")
                                    private User creator;

                                        @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
                                            private List<Move> moves;

                                                public Game() {
                                                        this.createdAt = LocalDateTime.now();
                                                                this.status = "ONGOING";
                                                                        this.currentTurn = "WHITE";
                                                                            }

                                                                                // Getters and Setters

                                                                                    public Long getId() { return id; }

                                                                                        public String getStatus() { return status; }

                                                                                            public void setStatus(String status) { this.status = status; }

                                                                                                public String getCurrentTurn() { return currentTurn; }

                                                                                                    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }

                                                                                                        public LocalDateTime getCreatedAt() { return createdAt; }

                                                                                                            public User getCreator() { return creator; }

                                                                                                                public void setCreator(User creator) { this.creator = creator; }

                                                                                                                    public List<Move> getMoves() { return moves; }

                                                                                                                        public void setMoves(List<Move> moves) { this.moves = moves; }
                                                                                                                        }