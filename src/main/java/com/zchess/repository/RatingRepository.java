package com.zchess.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zchess.entity.Rating;
import com.zchess.entity.User;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUser(User user);
    List<Rating> findAllByOrderByRatingDesc();
}