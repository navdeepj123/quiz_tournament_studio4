package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndTournamentId(Long userId, Long tournamentId); // Changed to Optional<Like>
    boolean existsByUserIdAndTournamentId(Long userId, Long tournamentId);
    int countByTournamentId(Long tournamentId);
}