// src/main/java/com/example/quiz_tournament/payload/request/CreateTournamentRequest.java
package com.example.quiz_tournament.payload.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class CreateTournamentRequest {
    @NotBlank @Size(max = 100)
    private String name;

    @NotBlank @Size(max = 50)
    private String category;

    @NotBlank @Size(max = 20)
    private String difficulty;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}