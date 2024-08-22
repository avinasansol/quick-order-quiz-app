package com.quiz.order.repository;

import com.quiz.order.models.AppStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStatusRepository extends JpaRepository<AppStatus, Long> {

    @Query("SELECT a.appStatValue FROM AppStatus a WHERE a.appStatId = :appStatId")
    Character findAppStatValueById(@Param("appStatId") Long appStatId);
}
