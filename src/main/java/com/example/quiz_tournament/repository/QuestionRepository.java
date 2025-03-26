package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {}
