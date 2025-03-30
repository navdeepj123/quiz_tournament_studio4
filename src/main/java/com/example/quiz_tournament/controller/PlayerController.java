package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.*;
import com.example.quiz_tournament.payload.response.*;
import com.example.quiz_tournament.repository.*;
import com.example.quiz_tournament.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/player")
@PreAuthorize("hasRole('PLAYER')")
public class PlayerController {

    @Autowired
    private QuizTournamentRepository tournamentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private LikeRepository likeRepository;

    @GetMapping("/tournaments")
    public ResponseEntity<Map<String, List<TournamentResponse>>> getTournaments() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

            LocalDateTime now = LocalDateTime.now();
            List<QuizTournament> all = tournamentRepository.findAll();
            List<Participation> participations = participationRepository.findByUserId(userDetails.getId());

            Map<String, List<TournamentResponse>> result = new HashMap<>();
            result.put("ongoing", new ArrayList<>());
            result.put("upcoming", new ArrayList<>());
            result.put("past", new ArrayList<>());
            result.put("participated", new ArrayList<>());

            for (QuizTournament tournament : all) {
                boolean isLiked = likeRepository.existsByUserIdAndTournamentId(
                        userDetails.getId(),
                        tournament.getId()
                );

                TournamentResponse tr = new TournamentResponse(
                        tournament.getId(),
                        tournament.getName(),
                        tournament.getCategory(),
                        tournament.getDifficulty(),
                        tournament.getStartDate(),
                        tournament.getEndDate(),
                        tournament.getQuestions().size(),
                        isLiked
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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/participate/{tournamentId}")
    public ResponseEntity<?> participate(@PathVariable Long tournamentId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

            QuizTournament tournament = tournamentRepository.findById(tournamentId)
                    .orElseThrow(() -> new RuntimeException("Tournament not found"));

            if (!tournament.isActive()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Tournament is not active"));
            }

            if (participationRepository.existsByUserIdAndTournamentId(
                    userDetails.getId(),
                    tournamentId
            )) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Already participated"));
            }

            Participation participation = new Participation();
            participation.setUserId(userDetails.getId());
            tournament.addParticipation(participation);
            participationRepository.save(participation);

            List<QuestionDto> questions = tournament.getQuestions().stream()
                    .map(q -> new QuestionDto(
                            q.getId(),
                            q.getText(),
                            q.getType().toString(),
                            q.getCorrectAnswer()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Error participating in tournament"));
        }
    }

    @PostMapping("/answer/{questionId}")
    public ResponseEntity<AnswerResponse> submitAnswer(
            @PathVariable Long questionId,
            @RequestParam String answer) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Participation participation = participationRepository
                    .findByUserIdAndTournamentId(
                            userDetails.getId(),
                            question.getTournament().getId()
                    );

            if (participation == null) {
                return ResponseEntity.badRequest()
                        .body(new AnswerResponse("Not participating"));
            }

            if (participation.isCompleted()) {
                return ResponseEntity.badRequest()
                        .body(new AnswerResponse("Tournament already completed"));
            }

            boolean isCorrect = answer.equalsIgnoreCase(question.getCorrectAnswer());
            participation.answerQuestion(questionId, isCorrect);
            participationRepository.save(participation);

            return ResponseEntity.ok(new AnswerResponse(
                    isCorrect,
                    isCorrect ? "Correct!" : "Incorrect! Correct answer: " + question.getCorrectAnswer(),
                    participation.getScore()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new AnswerResponse("Error submitting answer"));
        }
    }

    @PostMapping("/like/{tournamentId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long tournamentId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

            Optional<Like> likeOpt = likeRepository.findByUserIdAndTournamentId(
                    userDetails.getId(),
                    tournamentId
            );

            if (likeOpt.isPresent()) {
                likeRepository.delete(likeOpt.get());
                return ResponseEntity.ok(new MessageResponse("Tournament unliked"));
            } else {
                Like newLike = new Like();
                newLike.setUserId(userDetails.getId());
                newLike.setTournament(tournamentRepository.getReferenceById(tournamentId));
                likeRepository.save(newLike);
                return ResponseEntity.ok(new MessageResponse("Tournament liked"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Error toggling like"));
        }
    }
}