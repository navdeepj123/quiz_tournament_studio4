package com.example.quiz_tournament.payload.response;

public class AnswerResponse {
    private boolean correct;
    private String feedback;
    private int currentScore;
    private String error;

    public AnswerResponse(boolean correct, String feedback, int currentScore) {
        this.correct = correct;
        this.feedback = feedback;
        this.currentScore = currentScore;
    }

    public AnswerResponse(String error) {
        this.error = error;
    }

    // Getters
    public boolean isCorrect() { return correct; }
    public String getFeedback() { return feedback; }
    public int getCurrentScore() { return currentScore; }
    public String getError() { return error; }
}