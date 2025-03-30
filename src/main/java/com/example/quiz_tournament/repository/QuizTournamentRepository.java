// src/main/java/com/example/quiz_tournament/repository/QuizTournamentRepository.java
package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.QuizTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizTournamentRepository extends JpaRepository<QuizTournament, Long> {
}