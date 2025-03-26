package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.Question;
import com.example.quiz_tournament.model.Quiz;
import com.example.quiz_tournament.repository.QuestionRepository;
import com.example.quiz_tournament.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/quiz")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RestTemplate restTemplate;

    // ✅ Create Quiz + Fetch Questions from OpenTDB
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
                List<String> incorrects = (List<String>) result.get("incorrect_answers");
                q.setIncorrectAnswers(String.join(",", incorrects));
                q.setQuiz(savedQuiz);
                questionRepository.save(q);
            }

            return ResponseEntity.ok("Quiz created with 10 questions!");
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
}
