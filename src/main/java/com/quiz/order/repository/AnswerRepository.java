package com.quiz.order.repository;

import com.quiz.order.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByUserIdAndQuestionId(Long userId, Long questionId);
}
