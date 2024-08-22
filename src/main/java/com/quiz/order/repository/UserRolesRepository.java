package com.quiz.order.repository;

import com.quiz.order.models.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    Optional<UserRoles> findByUserId(Long userId);
}
