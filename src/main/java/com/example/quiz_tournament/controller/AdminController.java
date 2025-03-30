// src/main/java/com/example/quiz_tournament/controller/AdminController.java
package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.ERole;
import com.example.quiz_tournament.model.Role;
import com.example.quiz_tournament.model.User;
import com.example.quiz_tournament.payload.request.AdminCreateRequest;
import com.example.quiz_tournament.payload.response.MessageResponse;
import com.example.quiz_tournament.repository.RoleRepository;
import com.example.quiz_tournament.repository.UserRepository;
import com.example.quiz_tournament.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminCreateRequest request) {
        // Validate username/email
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new admin user
        User admin = new User(
                request.getUsername(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
        );

        // Set admin role
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Admin role not found."));
        admin.getRoles().add(adminRole);

        // Save optional fields
        if (request.getProfileImageUrl() != null) {
            admin.setProfileImageUrl(request.getProfileImageUrl());
        }

        userRepository.save(admin);

        return ResponseEntity.ok(new MessageResponse("Admin user created successfully!"));
    }
}