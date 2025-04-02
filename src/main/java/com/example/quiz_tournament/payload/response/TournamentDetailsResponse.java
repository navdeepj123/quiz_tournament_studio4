package com.example.quiz_tournament.payload.response;

import java.time.LocalDateTime;

public class TournamentDetailsResponse {
    private Long id;
    private String name;
    private String category;
    private String difficulty;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int questionCount;
    private int totalParticipants;
    private int totalLikes;
    private boolean isLikedByUser;

    // All-arguments constructor
    public TournamentDetailsResponse(
            Long id,
            String name,
            String category,
            String difficulty,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int questionCount,
            int totalParticipants,
            int totalLikes,
            boolean isLikedByUser) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.questionCount = questionCount;
        this.totalParticipants = totalParticipants;
        this.totalLikes = totalLikes;
        this.isLikedByUser = isLikedByUser;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }
}