package com.example.quiz_tournament.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "participations")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private QuizTournament tournament;

    private boolean completed = false;
    private int score = 0;

    @ElementCollection
    @CollectionTable(name = "user_answers", joinColumns = @JoinColumn(name = "participation_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "is_correct")
    private Map<Long, Boolean> answers = new HashMap<>();

    // Constructors
    public Participation() {}

    public Participation(Long userId, QuizTournament tournament) {
        this.userId = userId;
        this.tournament = tournament;
    }

    // Methods
    public void answerQuestion(Long questionId, boolean isCorrect) {
        answers.put(questionId, isCorrect);
        score = (int) answers.values().stream().filter(b -> b).count();
        if (answers.size() == tournament.getQuestions().size()) {
            completed = true;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public QuizTournament getTournament() { return tournament; }
    public void setTournament(QuizTournament tournament) { this.tournament = tournament; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public Map<Long, Boolean> getAnswers() { return answers; }
    public void setAnswers(Map<Long, Boolean> answers) { this.answers = answers; }
}