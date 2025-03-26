package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // No custom methods needed for now
}
