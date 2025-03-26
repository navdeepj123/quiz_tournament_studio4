package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {}
