package com.quiz.order.scheduler;

import com.quiz.order.repository.AppStatusRepository;
import com.quiz.order.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import com.quiz.order.models.Question;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Component
public class QuestionUpdateScheduler {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AppStatusRepository appStatusRepository;

    private TaskScheduler scheduler = new ConcurrentTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;

    private boolean isSchedulerRunning = false;

    @PostConstruct
    public void init() {
        // Start the scheduler based on initial app status value
        Character appStatValue = appStatusRepository.findAppStatValueById(1L);
        if (appStatValue != null && appStatValue == 'Y') {
            startScheduler();
        }
    }

    public void startScheduler() {
        if (!isSchedulerRunning) {
            scheduledFuture = scheduler.scheduleAtFixedRate(this::updateQuestionTimes, 1000);
            isSchedulerRunning = true;
            System.out.println("Scheduler started.");
        }
    }

    public void stopScheduler() {
        if (isSchedulerRunning && scheduledFuture != null) {
            scheduledFuture.cancel(false);
            isSchedulerRunning = false;
            System.out.println("Scheduler stopped.");
        }
    }

    public void updateQuestionTimes() {
        System.out.println("Scheduler is executing");
        questionRepository.decrementTimeLeftForActiveQuestions(Arrays.asList('P', 'Y', 'C'));
        questionRepository.activatePendingQuestions();
        questionRepository.completeActiveQuestions();

        Character appStatValue = appStatusRepository.findAppStatValueById(1L);
        if (appStatValue != null && appStatValue == 'Y') {
            boolean noActiveOrPendingQuestions = questionRepository.findOrderedQuestions(Arrays.asList('Y', 'P')).isEmpty();

            if (noActiveOrPendingQuestions) {
                List<Question> inactiveQuestions = questionRepository.findInactiveQuestions();
                if (!inactiveQuestions.isEmpty()) {
                    Question nextQuestion = inactiveQuestions.get(0);
                    questionRepository.activateNextInactiveQuestion(nextQuestion.getQuestionId());
                } else {
			// Stop the scheduler if there are no more inactive questions
			stopScheduler();
		}
            }
        }
    }
}
