package com.zchess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zchess.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}