package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // ✅ must exist for create-admin
    Optional<User> findByEmail(String email);        // ✅ now added for UserService usage

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

