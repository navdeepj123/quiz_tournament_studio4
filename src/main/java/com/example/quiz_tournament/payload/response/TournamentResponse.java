package com.example.quiz_tournament.payload.response;

import java.time.LocalDateTime;

public class TournamentResponse {
    private Long id;
    private String name;
    private String category;
    private String difficulty;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int questionCount;
    private boolean liked;

    // Updated constructor with liked parameter
    public TournamentResponse(Long id, String name, String category, String difficulty,
                              LocalDateTime startDate, LocalDateTime endDate,
                              int questionCount, boolean liked) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.questionCount = questionCount;
        this.liked = liked;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public int getQuestionCount() { return questionCount; }
    public boolean isLiked() { return liked; }
}