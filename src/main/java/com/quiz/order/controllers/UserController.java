package com.quiz.order.controllers;

import com.quiz.order.dto.UserDto;
import com.quiz.order.models.UserEntity;
import com.quiz.order.models.UserRoles;
import com.quiz.order.repository.UserRepository;
import com.quiz.order.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @GetMapping("/me")
    public ResponseEntity<Object> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // Fetch the user from the repository
            return userRepository.findByUsername(username)
                    .map(user -> {
                        // Fetch the user role
                        String userRole = userRolesRepository.findByUserId(user.getId())
                                .map(role -> {
                                    switch (role.getRoleId()) {
                                        case 2:
                                            return "Admin";
                                        case 1:
                                            return "User";
                                        default:
                                            return "Unknown";
                                    }
                                })
                                .orElse("No Role Assigned");

                        // Return the user details along with the role
                        UserDto userDto = new UserDto(user.getUsername(), user.getName(), user.getPoints(), userRole);
                        return new ResponseEntity<Object>(userDto, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>("No user authenticated", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        // Sort users by points in descending order
        users.sort((u1, u2) -> Integer.compare(u2.getPoints(), u1.getPoints()));

        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity user : users) {
            String userRole = userRolesRepository.findByUserId(user.getId())
                    .map(role -> {
                        switch (role.getRoleId()) {
                            case 1:
                                return "Admin";
                            case 2:
                                return "User";
                            default:
                                return "Unknown";
                        }
                    })
                    .orElse("No Role Assigned");

            userDtos.add(new UserDto(user.getUsername(), user.getName(), user.getPoints(), userRole));
        }

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }
}
