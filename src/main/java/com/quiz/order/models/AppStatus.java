package com.quiz.order.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "appstatus")
@Data
@NoArgsConstructor
public class AppStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appstat_id", nullable = false)
    private Long appStatId;

    @Column(name = "value", nullable = false, columnDefinition = "varchar(1)")
    private Character appStatValue;

    @Column(name = "description", nullable = true, columnDefinition = "varchar(255)")
    private String appStatDesc;
}
