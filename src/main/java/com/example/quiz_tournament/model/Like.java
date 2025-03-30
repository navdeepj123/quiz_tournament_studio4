package com.example.quiz_tournament.model;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private QuizTournament tournament;

    public Like(){
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public QuizTournament getTournament() { return tournament; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setTournament(QuizTournament tournament) { this.tournament = tournament; }
}