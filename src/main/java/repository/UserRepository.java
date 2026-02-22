package com.zchess.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zchess.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
}