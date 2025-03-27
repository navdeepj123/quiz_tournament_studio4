package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.Question;
import com.example.quiz_tournament.model.Quiz;
import com.example.quiz_tournament.model.User;
import com.example.quiz_tournament.repository.QuestionRepository;
import com.example.quiz_tournament.repository.QuizRepository;
import com.example.quiz_tournament.repository.UserRepository;
import com.example.quiz_tournament.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/admin/quiz")
public class QuizController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RestTemplate restTemplate;

    // ✅ Create Quiz + Fetch Questions + Email Users
    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz) {
        try {
            Quiz savedQuiz = quizRepository.save(quiz);

            String url = "https://opentdb.com/api.php?amount=10&category=9&difficulty=" + quiz.getDifficulty() + "&type=multiple";
            Map response = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            for (Map<String, Object> result : results) {
                Question q = new Question();
                q.setQuestion((String) result.get("question"));
                q.setCorrectAnswer((String) result.get("correct_answer"));

                // Extract and shuffle options
                List<String> options = new ArrayList<>((List<String>) result.get("incorrect_answers"));
                options.add((String) result.get("correct_answer"));
                Collections.shuffle(options);

                q.setOption1(options.get(0));
                q.setOption2(options.get(1));
                q.setOption3(options.get(2));
                q.setOption4(options.get(3));
                q.setQuiz(savedQuiz);
                questionRepository.save(q);
            }

            // ✅ Email all users (excluding admins)
            List<User> users = userRepository.findAll();
            for (User u : users) {
                if (!u.getRole().equalsIgnoreCase("ADMIN")) {
                    emailService.sendEmail(
                            u.getEmail(),
                            "New Quiz Available!",
                            "Hi " + u.getFirstName() + ",\n\nA new quiz titled '" + savedQuiz.getName() + "' is now live!\n\nGo check it out!"
                    );
                }
            }

            return ResponseEntity.ok("Quiz created and emails sent!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

    // ✅ View All Quizzes
    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return ResponseEntity.ok(quizzes);
    }

    // ✅ View Questions for a Quiz
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<Question>> getQuestionsByQuizId(@PathVariable Long id) {
        return quizRepository.findById(id).map(quiz -> {
            List<Question> questions = quiz.getQuestions();
            return ResponseEntity.ok(questions);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ Update Quiz
    @PutMapping("/quizzes/{id}")
    public ResponseEntity<?> updateQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
        return quizRepository.findById(id).map(existingQuiz -> {
            existingQuiz.setName(updatedQuiz.getName());
            existingQuiz.setCategory(updatedQuiz.getCategory());
            existingQuiz.setDifficulty(updatedQuiz.getDifficulty());
            existingQuiz.setStartDate(updatedQuiz.getStartDate());
            existingQuiz.setEndDate(updatedQuiz.getEndDate());
            quizRepository.save(existingQuiz);
            return ResponseEntity.ok("Quiz updated successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found"));
    }

    // ✅ Delete Quiz
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        return quizRepository.findById(id).map(q -> {
            quizRepository.deleteById(id);
            return ResponseEntity.ok("Quiz deleted successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found"));
    }
}
