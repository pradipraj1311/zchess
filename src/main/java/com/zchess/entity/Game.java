package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String status;
                    private String result;

                        @ManyToOne
                            @JoinColumn(name="user_id")
                                private User user;

                                    public Long getId() { return id; }

                                        public String getStatus() { return status; }
                                            public void setStatus(String status) { this.status = status; }

                                                public String getResult() { return result; }
                                                    public void setResult(String result) { this.result = result; }

                                                        public User getUser() { return user; }
                                                            public void setUser(User user) { this.user = user; }
                                                            }