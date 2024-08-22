package com.quiz.order.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "ans_1", nullable = true, columnDefinition = "varchar(1) default NULL")
    private Character ans1;

    @Column(name = "ans_2", nullable = true, columnDefinition = "varchar(1) default NULL")
    private Character ans2;

    @Column(name = "ans_3", nullable = true, columnDefinition = "varchar(1) default NULL")
    private Character ans3;

    @Column(name = "ans_4", nullable = true, columnDefinition = "varchar(1) default NULL")
    private Character ans4;

    @Column(name = "points")
    private Integer points;
}
