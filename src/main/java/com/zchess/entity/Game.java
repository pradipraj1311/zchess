package com.zchess.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String playerWhite;
	private String playerBlack;

	private String status;
	private String currentTurn;
	private LocalDateTime createdAt;
	@OneToMany(mappedBy="game",cascade=CascadeType.ALL)
	private List<Move> moves;

	public Game() {
		this.createdAt=LocalDateTime.now();
		this.status="Ongoing";
		this.currentTurn="white";
	}

	public Game(String playerWhite, String playerBlack) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		this.status ="Ongoing";
		this.currentTurn="white";
		this.createdAt=LocalDateTime.now();
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
	public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }
}
