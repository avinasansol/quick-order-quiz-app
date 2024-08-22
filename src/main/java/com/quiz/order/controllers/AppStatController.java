package com.quiz.order.controllers;

import com.quiz.order.models.AppStatus;
import com.quiz.order.models.UserRoles;
import com.quiz.order.repository.AppStatusRepository;
import com.quiz.order.repository.UserRolesRepository;
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

    @PostMapping("/")
    public ResponseEntity<?> updateAppStat(@RequestBody AppStatRequest appStatRequest, Authentication authentication) {
        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Extract user ID from the authentication token
        Long userId = jwtUtils.getUserIdFromAuth(authentication);

        // Fetch the user role from the database
        Optional<UserRoles> userRolesOptional = userRolesRepository.findByUserId(userId);
        if (userRolesOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Check if the role_id is not 2
        UserRoles userRoles = userRolesOptional.get();
        if (userRoles.getRoleId() != 2) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Find the AppStatus with appStatId = 1
        AppStatus appStatus = appStatusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("AppStatus with id 1 not found"));

        // Update the appStatValue if the activate parameter is provided
        if (appStatRequest.getActivate() != null) {
            if (appStatRequest.getActivate().equalsIgnoreCase("Y") ||
                    appStatRequest.getActivate().equalsIgnoreCase("N")) {
                appStatus.setAppStatValue(appStatRequest.getActivate().charAt(0));
                appStatusRepository.save(appStatus);
                return ResponseEntity.ok(appStatus.getAppStatValue());
            } else {
                return ResponseEntity.badRequest().body("Invalid value for activate. Only 'Y' or 'N' are allowed.");
            }
        }

        // Return the current value if no activation status is provided
        return ResponseEntity.ok(appStatus.getAppStatValue());
    }

    // DTO for handling the incoming POST request
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
