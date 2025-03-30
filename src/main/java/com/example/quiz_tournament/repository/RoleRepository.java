// src/main/java/com/example/quiz_tournament/repository/RoleRepository.java
package com.example.quiz_tournament.repository;

import com.example.quiz_tournament.model.ERole;
import com.example.quiz_tournament.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}