package com.example.quiz_tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "quiz_tournaments")
public class QuizTournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String category;

    @NotBlank
    @Size(max = 20)
    private String difficulty; // "easy", "medium", "hard"

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    // Constructors
    public QuizTournament() {}

    public QuizTournament(String name, String category, String difficulty,
                          LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public List<Participation> getParticipations() { return participations; }
    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    public List<Like> getLikes() { return likes; }
    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    // Status methods - Single source of truth
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    public boolean isUpcoming() {
        return LocalDateTime.now().isBefore(startDate);
    }

    public boolean isPast() {
        return LocalDateTime.now().isAfter(endDate);
    }

    // Helper methods
    public void addQuestion(Question question) {
        questions.add(question);
        question.setTournament(this);
    }

    public boolean hasUserParticipated(Long userId) {
        return participations.stream()
                .anyMatch(p -> p.getUserId().equals(userId));
    }

    public boolean isLikedByUser(Long userId) {
        return likes.stream()
                .anyMatch(l -> l.getUserId().equals(userId));
    }

    // Participation management
    public void addParticipation(Participation participation) {
        participations.add(participation);
        participation.setTournament(this);
    }

    // Like management
    public void addLike(Like like) {
        likes.add(like);
        like.setTournament(this);
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setTournament(null);
    }
}