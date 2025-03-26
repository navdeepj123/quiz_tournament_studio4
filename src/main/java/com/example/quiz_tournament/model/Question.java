package com.example.quiz_tournament.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // ✅ Add this import
import jakarta.persistence.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String correctAnswer;
    private String incorrectAnswers;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonIgnore // ✅ This avoids infinite recursion when serializing
    private Quiz quiz;

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getIncorrectAnswers() { return incorrectAnswers; }
    public void setIncorrectAnswers(String incorrectAnswers) { this.incorrectAnswers = incorrectAnswers; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
}
