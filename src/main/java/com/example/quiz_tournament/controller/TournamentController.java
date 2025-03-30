package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.*;
import com.example.quiz_tournament.payload.request.CreateTournamentRequest;
import com.example.quiz_tournament.payload.request.UpdateTournamentRequest;
import com.example.quiz_tournament.payload.response.MessageResponse;
import com.example.quiz_tournament.payload.response.TournamentResponse;
import com.example.quiz_tournament.repository.QuizTournamentRepository;
import com.example.quiz_tournament.repository.UserRepository;
import com.example.quiz_tournament.security.services.EmailService;
import com.example.quiz_tournament.security.services.UserDetailsImpl;
import com.example.quiz_tournament.service.OpenTDBService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    @Autowired
    private QuizTournamentRepository tournamentRepository;

    @Autowired
    private OpenTDBService openTDBService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTournament(@Valid @RequestBody CreateTournamentRequest request) {
        // Create new tournament
        QuizTournament tournament = new QuizTournament();
        tournament.setName(request.getName());
        tournament.setCategory(request.getCategory());
        tournament.setDifficulty(request.getDifficulty());
        tournament.setStartDate(request.getStartDate());
        tournament.setEndDate(request.getEndDate());

        // Fetch questions from OpenTDB
        List<Question> questions = openTDBService.fetchQuestions(
                request.getCategory(),
                request.getDifficulty()
        );

        // Add questions to tournament
        for (Question question : questions) {
            tournament.addQuestion(question);
        }

        // Save tournament
        tournamentRepository.save(tournament);

        // Send email notifications to all non-admin users
        sendTournamentNotifications(tournament);

        return ResponseEntity.ok(new MessageResponse("Tournament created and notifications sent!"));
    }

    @GetMapping
    public ResponseEntity<List<TournamentResponse>> getAllTournaments(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        List<QuizTournament> tournaments = tournamentRepository.findAll();
        return ResponseEntity.ok(tournaments.stream()
                .map(t -> convertToResponse(t, userId))
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTournament(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTournamentRequest request) {

        QuizTournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        if (request.getName() != null) {
            tournament.setName(request.getName());
        }
        if (request.getStartDate() != null) {
            tournament.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            tournament.setEndDate(request.getEndDate());
        }

        tournamentRepository.save(tournament);
        return ResponseEntity.ok(new MessageResponse("Tournament updated successfully!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTournament(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean confirm) {

        if (!confirm) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Add confirm=true parameter to delete"));
        }

        tournamentRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Tournament deleted successfully!"));
    }

    private void sendTournamentNotifications(QuizTournament tournament) {
        userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getName() == ERole.ROLE_ADMIN))
                .forEach(user -> {
                    emailService.sendTournamentNotification(
                            user.getEmail(),
                            tournament.getName()
                    );
                });
    }

    private TournamentResponse convertToResponse(QuizTournament tournament, Long userId) {
        boolean isLiked = tournament.getLikes().stream()
                .anyMatch(like -> like.getUserId().equals(userId));

        return new TournamentResponse(
                tournament.getId(),
                tournament.getName(),
                tournament.getCategory(),
                tournament.getDifficulty(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getQuestions().size(),
                isLiked
        );
    }
}