package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.*;
import com.example.quiz_tournament.payload.request.UpdateUserRequest;
import com.example.quiz_tournament.payload.response.*;
import com.example.quiz_tournament.repository.*;
import com.example.quiz_tournament.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    QuizTournamentRepository tournamentRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    LikeRepository likeRepository;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        if (updateRequest.getUsername() != null && !updateRequest.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            }
            user.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getPassword() != null) {
            user.setPassword(encoder.encode(updateRequest.getPassword()));
        }

        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }

        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }

        if (updateRequest.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updateRequest.getProfileImageUrl());
        }

        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        if (updateRequest.getBio() != null) {
            user.setBio(updateRequest.getBio());
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    // Player-specific endpoints
    @GetMapping("/tournaments")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<Map<String, List<TournamentResponse>>> getPlayerTournaments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        List<QuizTournament> allTournaments = tournamentRepository.findAll();
        List<Participation> participations = participationRepository.findByUserId(userDetails.getId());

        Map<String, List<TournamentResponse>> result = new HashMap<>();
        result.put("ongoing", new ArrayList<>());
        result.put("upcoming", new ArrayList<>());
        result.put("past", new ArrayList<>());
        result.put("participated", new ArrayList<>());

        for (QuizTournament tournament : allTournaments) {
            TournamentResponse tr = new TournamentResponse(
                    tournament.getId(),
                    tournament.getName(),
                    tournament.getCategory(),
                    tournament.getDifficulty(),
                    tournament.getStartDate(),
                    tournament.getEndDate(),
                    tournament.getQuestions().size(),
                    likeRepository.existsByUserIdAndTournamentId(userDetails.getId(), tournament.getId())
            );

            boolean participated = participations.stream()
                    .anyMatch(p -> p.getTournament().getId().equals(tournament.getId()));

            if (participated) {
                result.get("participated").add(tr);
            } else if (tournament.isActive()) {
                result.get("ongoing").add(tr);
            } else if (tournament.isUpcoming()) {
                result.get("upcoming").add(tr);
            } else {
                result.get("past").add(tr);
            }
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/tournaments/{tournamentId}/participate")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<?> participateInTournament(@PathVariable Long tournamentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        QuizTournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        if (!tournament.isActive()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tournament is not active"));
        }

        if (participationRepository.existsByUserIdAndTournamentId(userDetails.getId(), tournamentId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Already participated"));
        }

        Participation participation = new Participation(userDetails.getId(), tournament);
        participationRepository.save(participation);

        return ResponseEntity.ok(tournament.getQuestions().stream()
                .map(q -> new QuestionDto(q.getId(), q.getText(), q.getType().toString(),q.getCorrectAnswer()))
                .collect(Collectors.toList()));
    }

    @PostMapping("/tournaments/{tournamentId}/like")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<?> toggleTournamentLike(@PathVariable Long tournamentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        QuizTournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        Optional<Like> existingLike = likeRepository.findByUserIdAndTournamentId(userDetails.getId(), tournamentId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return ResponseEntity.ok(new MessageResponse("Tournament unliked"));
        } else {
            Like newLike = new Like();
            likeRepository.save(newLike);
            return ResponseEntity.ok(new MessageResponse("Tournament liked"));
        }
    }

}