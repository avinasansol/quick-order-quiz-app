package com.quiz.order.repository;

import com.quiz.order.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
}
