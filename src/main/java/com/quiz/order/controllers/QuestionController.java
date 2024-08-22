package com.quiz.order.controllers;

import com.quiz.order.dto.AnswerDto;
import com.quiz.order.dto.QuestionDto;
import com.quiz.order.models.Question;
import com.quiz.order.models.Answer;
import com.quiz.order.repository.AnswerRepository;
import com.quiz.order.repository.QuestionRepository;
import com.quiz.order.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/{status}")
    public ResponseEntity<List<QuestionDto>> getQuestionsByStatus(
            @PathVariable("status") String status, Authentication authentication) {

        Long userId = jwtUtils.getUserIdFromAuth(authentication);
        char statusChar = getStatusChar(status);

        if (statusChar == '\0') {
            return ResponseEntity.badRequest().build();
        }

        List<Question> questions = questionRepository.findOrderedQuestions(List.of(statusChar));

        List<QuestionDto> questionDtos = questions.stream().map(question -> {
            QuestionDto questionDto = new QuestionDto(question);
            Optional<Answer> answerOpt = answerRepository.findByUserIdAndQuestionId(userId, question.getQuestionId());
            answerOpt.ifPresent(answer -> questionDto.setAnswerDto(new AnswerDto(answer)));
            questionDto.updateForActiveStatus();
            return questionDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(questionDtos);
    }

    private char getStatusChar(String status) {
        switch (status.toLowerCase()) {
            case "p":
                return 'P';
            case "y":
                return 'Y';
            case "c":
                return 'C';
            default:
                return '\0';
        }
    }
}
