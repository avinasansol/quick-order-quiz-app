package com.quiz.order.dto;

public class UserDto {
    private String username;
    private String name;
    private int points;
    private String role;

    public UserDto(String username, String name, int points, String role) {
        this.username = username;
        this.name = name;
        this.points = points;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUserRole() {
        return role;
    }

    public void setUserRole(String role) {
        this.role = role;
    }
}
