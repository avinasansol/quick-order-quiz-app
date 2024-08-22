package com.quiz.order.controllers;

import com.quiz.order.models.Answer;
import com.quiz.order.models.Question;
import com.quiz.order.models.UserEntity;
import com.quiz.order.repository.AnswerRepository;
import com.quiz.order.repository.QuestionRepository;
import com.quiz.order.repository.UserRepository;
import com.quiz.order.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/{questionId}")
    public ResponseEntity<?> submitAnswer(@PathVariable("questionId") Long questionId,
                                          @RequestBody AnswerRequest answerRequest,
                                          Authentication authentication) {

        // Check if the questionId is valid
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (!questionOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Invalid questionId");
        }

        Question question = questionOpt.get();

        // Check if active is 'Y' or (active is 'C' and time_left > 0)
        if (question.getActive() == 'Y' || (question.getActive() == 'C' && question.getTimeLeft() > 0)) {

            // Extract user ID from the authentication token
            Long userId = jwtUtils.getUserIdFromAuth(authentication);

            // Determine the values for ans_1, ans_2, ans_3, and ans_4
            Character ans1 = null, ans2 = null, ans3 = null, ans4 = null;
            if (answerRequest.getOptionA() != null) {
                switch (answerRequest.getOptionA()) {
                    case "1":
                        ans1 = 'A';
                        break;
                    case "2":
                        ans2 = 'A';
                        break;
                    case "3":
                        ans3 = 'A';
                        break;
                    case "4":
                        ans4 = 'A';
                        break;
                }
            }

            if (answerRequest.getOptionB() != null) {
                switch (answerRequest.getOptionB()) {
                    case "1":
                        ans1 = 'B';
                        break;
                    case "2":
                        ans2 = 'B';
                        break;
                    case "3":
                        ans3 = 'B';
                        break;
                    case "4":
                        ans4 = 'B';
                        break;
                }
            }

            if (answerRequest.getOptionC() != null) {
                switch (answerRequest.getOptionC()) {
                    case "1":
                        ans1 = 'C';
                        break;
                    case "2":
                        ans2 = 'C';
                        break;
                    case "3":
                        ans3 = 'C';
                        break;
                    case "4":
                        ans4 = 'C';
                        break;
                }
            }

            if (answerRequest.getOptionD() != null) {
                switch (answerRequest.getOptionD()) {
                    case "1":
                        ans1 = 'D';
                        break;
                    case "2":
                        ans2 = 'D';
                        break;
                    case "3":
                        ans3 = 'D';
                        break;
                    case "4":
                        ans4 = 'D';
                        break;
                }
            }

            // Calculate points based on correct answers
            int points = 0;
            // Validate the time value and add it as a bonus if it's greater than 0
            int bonusPoints = 0;

            if (answerRequest.getTime() != null && answerRequest.getTime() > 0) {
                bonusPoints = answerRequest.getTime();
            }

            if (ans1 != null && ans1.equals(question.getCorrectAns1()) &&
                    ans2 != null && ans2.equals(question.getCorrectAns2()) &&
                    ans3 != null && ans3.equals(question.getCorrectAns3()) &&
                    ans4 != null && ans4.equals(question.getCorrectAns4())) {
                points = 25;
                // Add bonus points to the total points
                points += bonusPoints;
            }

            // Create and save the Answer entity
            Answer answer = new Answer();
            answer.setUserId(userId);
            answer.setQuestionId(questionId);
            answer.setAns1(ans1);
            answer.setAns2(ans2);
            answer.setAns3(ans3);
            answer.setAns4(ans4);
            answer.setPoints(points); // Set points including bonus

            answerRepository.save(answer);

            // Update the user's points
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();
                user.setPoints(user.getPoints() + points); // Add the new points to the existing points
                userRepository.save(user);
            }

            return ResponseEntity.ok("Answer submitted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Question is not active. Cannot submit the answer.");
        }
    }

    // DTO for handling the incoming POST request
    public static class AnswerRequest {
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private Integer time;

        public String getOptionA() {
            return optionA;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }
    }
}
