package com.zchess.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // playerWhite - User saathe linked (foreign key)
    @ManyToOne
    @JoinColumn(name = "player_white")
    private User playerWhite;

    // playerBlack - User saathe linked (foreign key)
    @ManyToOne
    @JoinColumn(name = "player_black")
    private User playerBlack;

    private String status;
    private String currentTurn;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Move> moves;

    public Game() {
        this.createdAt = LocalDateTime.now();
        this.status = "Ongoing";
        this.currentTurn = "white";
    }

    public Game(User playerWhite, User playerBlack) {
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.status = "Ongoing";
        this.currentTurn = "white";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public User getPlayerWhite() { return playerWhite; }
    public void setPlayerWhite(User playerWhite) { this.playerWhite = playerWhite; }

    public User getPlayerBlack() { return playerBlack; }
    public void setPlayerBlack(User playerBlack) { this.playerBlack = playerBlack; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Move> getMoves() { return moves; }
    public void setMoves(List<Move> moves) { this.moves = moves; }
}