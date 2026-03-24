package com.zchess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zchess.entity.Game;
import com.zchess.entity.User;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayerWhite(User playerWhite);
    List<Game> findByPlayerBlack(User playerBlack);
}