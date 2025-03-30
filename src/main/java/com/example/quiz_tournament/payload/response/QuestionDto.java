package com.example.quiz_tournament.payload.response;

public class QuestionDto {
    private Long id;
    private String text;
    private String type;
    private String correctAnswer;  // Add this field

    // Updated constructor with correctAnswer
    public QuestionDto(Long id, String text, String type, String correctAnswer) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.correctAnswer = correctAnswer;
    }

    // Getters
    public Long getId() { return id; }
    public String getText() { return text; }
    public String getType() { return type; }
    public String getCorrectAnswer() { return correctAnswer; }
}