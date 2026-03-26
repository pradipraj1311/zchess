package com.zchess.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one user - one rating
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // starting rating 1000
    private int rating = 1000;

    private int wins = 0;
    private int losses = 0;
    private int draws = 0;
    private int gamesPlayed = 0;

    public Rating() {}

    public Rating(User user) {
        this.user = user;
        this.rating = 1000;
    }

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public int getDraws() { return draws; }
    public void setDraws(int draws) { this.draws = draws; }

    public int getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }
}