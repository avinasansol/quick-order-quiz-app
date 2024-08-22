package com.quiz.order.dto;

import com.quiz.order.models.Answer;
import lombok.Data;

@Data
public class AnswerDto {
    private Character ans1;
    private Character ans2;
    private Character ans3;
    private Character ans4;
    private Integer points;

    public AnswerDto(Answer answer) {
        this.ans1 = answer.getAns1();
        this.ans2 = answer.getAns2();
        this.ans3 = answer.getAns3();
        this.ans4 = answer.getAns4();
        this.points = answer.getPoints();
    }
}
