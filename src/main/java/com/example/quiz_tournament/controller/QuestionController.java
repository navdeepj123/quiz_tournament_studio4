package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.Question;
import com.example.quiz_tournament.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    // ✅ Delete Question by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        return questionRepository.findById(id)
                .map(q -> {
                    questionRepository.deleteById(id);
                    return ResponseEntity.ok("Question deleted successfully!");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found"));
    }

    // ✅ Update Question by ID (updated to use option1–4)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion) {
        return questionRepository.findById(id).map(existingQuestion -> {
            existingQuestion.setQuestion(updatedQuestion.getQuestion());
            existingQuestion.setOption1(updatedQuestion.getOption1());
            existingQuestion.setOption2(updatedQuestion.getOption2());
            existingQuestion.setOption3(updatedQuestion.getOption3());
            existingQuestion.setOption4(updatedQuestion.getOption4());
            existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            questionRepository.save(existingQuestion);
            return ResponseEntity.ok("Question updated successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found"));
    }
}
