// src/main/java/com/example/quiz_tournament/model/Question.java
package com.example.quiz_tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String text;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String correctAnswer;

    @ElementCollection
    @CollectionTable(name = "incorrect_answers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "answer", columnDefinition = "TEXT")
    private List<String> incorrectAnswers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private QuizTournament tournament;

    public enum QuestionType {
        MULTIPLE, TRUE_FALSE
    }

    // Constructors
    public Question() {}

    public Question(String text, String correctAnswer, List<String> incorrectAnswers, QuestionType type) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public List<String> getIncorrectAnswers() { return incorrectAnswers; }
    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public QuestionType getType() { return type; }
    public void setType(QuestionType type) { this.type = type; }

    public QuizTournament getTournament() { return tournament; }
    public void setTournament(QuizTournament tournament) { this.tournament = tournament; }
}