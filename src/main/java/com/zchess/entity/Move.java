package com.zchess.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "moves")
public class Move {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int fromRow;
	private int fromCol;

	private int toRow;
	private int toCol;

	private int moveNumber;

	private String piece;
	private String notation;
	private LocalDateTime playedAt;

	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;

	public Move() {
		this.playedAt=LocalDateTime.now();
	}

	public Move(int fromRow, int fromCol, int toRow, int toCol, int moveNumber,String piece) {
		this.fromRow = fromRow;
		this.fromCol = fromCol;
		this.toRow = toRow;
		this.toCol = toCol;
		this.moveNumber = moveNumber;
		this.piece=piece;
		this.playedAt=LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public int getFromRow() {
		return fromRow;
	}

	public void setFromRow(int fromRow) {
		this.fromRow = fromRow;
	}

	public int getFromCol() {
		return fromCol;
	}

	public void setFromCol(int fromCol) {
		this.fromCol = fromCol;
	}

	public int getToRow() {
		return toRow;
	}

	public void setToRow(int toRow) {
		this.toRow = toRow;
	}

	public int getToCol() {
		return toCol;
	}

	public void setToCol(int toCol) {
		this.toCol = toCol;
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	public String getPiece() {
		return piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}
	public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game=game;
	}
}
