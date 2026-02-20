package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class User {

        @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
                private Long id;

                    private String username;
                        private String password;
                            private int rating = 1000;

                                public Long getId() { return id; }
                                    public String getUsername() { return username; }
                                        public void setUsername(String username) { this.username = username; }

                                            public String getPassword() { return password; }
                                                public void setPassword(String password) { this.password = password; }

                                                    public int getRating() { return rating; }
                                                        public void setRating(int rating) { this.rating = rating; }
}