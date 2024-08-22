package com.quiz.order.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "ques", nullable = false, columnDefinition = "varchar(255)")
    private String ques;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String optA;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String optB;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String optC;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String optD;

    @Column(name = "correct_ans_1", nullable = false, columnDefinition = "varchar(1)")
    private Character correctAns1;

    @Column(name = "correct_ans_2", nullable = false, columnDefinition = "varchar(1)")
    private Character correctAns2;

    @Column(name = "correct_ans_3", nullable = false, columnDefinition = "varchar(1)")
    private Character correctAns3;

    @Column(name = "correct_ans_4", nullable = false, columnDefinition = "varchar(1)")
    private Character correctAns4;

    @Column(name = "active", nullable = false, columnDefinition = "varchar(1)")
    private Character active;

    @Column(name = "time_left", nullable = false)
    private Integer timeLeft;
}
