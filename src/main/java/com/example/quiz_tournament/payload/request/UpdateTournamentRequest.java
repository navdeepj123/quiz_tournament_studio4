// src/main/java/com/example/quiz_tournament/payload/request/UpdateTournamentRequest.java
package com.example.quiz_tournament.payload.request;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class UpdateTournamentRequest {
    @Size(max = 100)
    private String name;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}