package com.quiz.order.controllers;

import com.quiz.order.models.AppStatus;
import com.quiz.order.models.UserRoles;
import com.quiz.order.repository.AppStatusRepository;
import com.quiz.order.repository.UserRolesRepository;
import com.quiz.order.scheduler.QuestionUpdateScheduler;
import com.quiz.order.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/appstat")
public class AppStatController {

    @Autowired
    private AppStatusRepository appStatusRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private QuestionUpdateScheduler questionUpdateScheduler;

    @PostMapping("/")
    public ResponseEntity<?> updateAppStat(@RequestBody AppStatRequest appStatRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Long userId = jwtUtils.getUserIdFromAuth(authentication);

        Optional<UserRoles> userRolesOptional = userRolesRepository.findByUserId(userId);
        if (userRolesOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UserRoles userRoles = userRolesOptional.get();
        if (userRoles.getRoleId() != 2) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        AppStatus appStatus = appStatusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("AppStatus with id 1 not found"));

        if (appStatRequest.getActivate() != null) {
            if (appStatRequest.getActivate().equalsIgnoreCase("Y")) {
                appStatus.setAppStatValue('Y');
                appStatusRepository.save(appStatus);
                questionUpdateScheduler.startScheduler();
                return ResponseEntity.ok(appStatus.getAppStatValue());
            } else if (appStatRequest.getActivate().equalsIgnoreCase("N")) {
                appStatus.setAppStatValue('N');
                appStatusRepository.save(appStatus);
                questionUpdateScheduler.stopScheduler();
                return ResponseEntity.ok(appStatus.getAppStatValue());
            } else {
                return ResponseEntity.badRequest().body("Invalid value for activate. Only 'Y' or 'N' are allowed.");
            }
        }

        return ResponseEntity.ok(appStatus.getAppStatValue());
    }

    public static class AppStatRequest {
        private String activate;

        public String getActivate() {
            return activate;
        }

        public void setActivate(String activate) {
            this.activate = activate;
        }
    }
}
