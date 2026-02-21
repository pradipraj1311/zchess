package com.zchess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zchess.entity.Move;

public interface MoveRepository extends JpaRepository<Move, Long> {
}