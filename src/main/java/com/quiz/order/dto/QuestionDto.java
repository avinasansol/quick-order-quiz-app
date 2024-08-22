package com.quiz.order.dto;

import com.quiz.order.models.Question;
import lombok.Data;

@Data
public class QuestionDto {
    private Long questionId;
    private String ques;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private Character correctAns1;
    private Character correctAns2;
    private Character correctAns3;
    private Character correctAns4;
    private Character active;
    private Integer timeLeft;
    private AnswerDto answerDto;

    public QuestionDto(Question question) {
        this.questionId = question.getQuestionId();
        this.ques = question.getQues();
        this.optA = question.getOptA();
        this.optB = question.getOptB();
        this.optC = question.getOptC();
        this.optD = question.getOptD();
        this.correctAns1 = question.getCorrectAns1();
        this.correctAns2 = question.getCorrectAns2();
        this.correctAns3 = question.getCorrectAns3();
        this.correctAns4 = question.getCorrectAns4();
        this.active = question.getActive();
        this.timeLeft = question.getTimeLeft();
    }

    public void updateForActiveStatus() {
        if (this.active == 'P') {
            // Only questionId and ques should be present
            this.optA = null;
            this.optB = null;
            this.optC = null;
            this.optD = null;
            this.correctAns1 = null;
            this.correctAns2 = null;
            this.correctAns3 = null;
            this.correctAns4 = null;
            if (this.answerDto != null) {
                this.answerDto = null;
            }
        } else if (this.active == 'Y') {
            // correctAns fields should be null
            this.correctAns1 = '\0';
            this.correctAns2 = '\0';
            this.correctAns3 = '\0';
            this.correctAns4 = '\0';
        }
    }
}
