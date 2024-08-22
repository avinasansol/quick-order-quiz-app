package com.quiz.order.scheduler;

import com.quiz.order.repository.AppStatusRepository;
import com.quiz.order.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.quiz.order.models.Question;

import java.util.Arrays;
import java.util.List;

@Component
public class QuestionUpdateScheduler {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AppStatusRepository appStatusRepository;

    @Scheduled(fixedRate = 1000)
    public void updateQuestionTimes() {
        // Decrement time_left for active questions
        questionRepository.decrementTimeLeftForActiveQuestions(Arrays.asList('P', 'Y', 'C'));

        // Check and activate pending questions
        questionRepository.activatePendingQuestions();

        // Check and complete active questions
        questionRepository.completeActiveQuestions();

        // Check if appStatValue is 'Y' for appStatId = 1
        Character appStatValue = appStatusRepository.findAppStatValueById(1L);

        if (appStatValue != null && appStatValue == 'Y') {
            // Check if there are no active or pending questions
            boolean noActiveOrPendingQuestions = questionRepository.findOrderedQuestions(Arrays.asList('Y', 'P')).isEmpty();

            if (noActiveOrPendingQuestions) {
                // Find any inactive question
                List<Question> inactiveQuestions = questionRepository.findInactiveQuestions();

                if (!inactiveQuestions.isEmpty()) {
                    // Activate the first inactive question
                    Question nextQuestion = inactiveQuestions.get(0);
                    questionRepository.activateNextInactiveQuestion(nextQuestion.getQuestionId());
                }
            }
        }
    }
}
