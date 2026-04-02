package com.zchess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.zchess.entity.Game;
import com.zchess.entity.Move;

public interface MoveRepository extends JpaRepository<Move, Long> {
    List<Move> findByGameId(Long gameId);

    @Transactional
    void deleteByGame(Game game);
}