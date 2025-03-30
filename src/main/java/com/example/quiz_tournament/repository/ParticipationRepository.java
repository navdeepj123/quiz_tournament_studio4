package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByUserIdAndTournamentId(Long userId, Long tournamentId);
    Participation findByUserIdAndTournamentId(Long userId, Long tournamentId);
    List<Participation> findByUserId(Long userId);
}