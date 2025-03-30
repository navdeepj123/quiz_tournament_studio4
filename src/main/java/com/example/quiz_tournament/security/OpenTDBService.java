// src/main/java/com/example/quiz_tournament/service/OpenTDBService.java
package com.example.quiz_tournament.service;

import com.example.quiz_tournament.model.Question;
import com.example.quiz_tournament.model.Question.QuestionType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class OpenTDBService {
    private static final String API_URL = "https://opentdb.com/api.php?amount=10&category={category}&difficulty={difficulty}&type=multiple";

    private final RestTemplate restTemplate;

    public OpenTDBService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Question> fetchQuestions(String category, String difficulty) {
        String url = API_URL
                .replace("{category}", category)
                .replace("{difficulty}", difficulty.toLowerCase());

        OpenTDBResponse response = restTemplate.getForObject(url, OpenTDBResponse.class);

        if (response == null || response.getResults() == null) {
            throw new RuntimeException("Failed to fetch questions from OpenTDB");
        }

        return convertToQuestions(response.getResults());
    }

    private List<Question> convertToQuestions(List<OpenTDBResponse.Result> results) {
        List<Question> questions = new ArrayList<>();
        for (OpenTDBResponse.Result result : results) {
            Question question = new Question();
            question.setText(decodeHtmlEntities(result.getQuestion()));
            question.setCorrectAnswer(decodeHtmlEntities(result.getCorrect_answer()));

            List<String> incorrectAnswers = new ArrayList<>();
            for (String answer : result.getIncorrect_answers()) {
                incorrectAnswers.add(decodeHtmlEntities(answer));
            }
            question.setIncorrectAnswers(incorrectAnswers);

            question.setType(result.getType().equals("boolean") ?
                    QuestionType.TRUE_FALSE : QuestionType.MULTIPLE);

            questions.add(question);
        }
        return questions;
    }

    private String decodeHtmlEntities(String text) {
        return text.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }

    // Inner class for API response
    public static class OpenTDBResponse {
        private List<Result> results;

        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }

        public static class Result {
            private String category;
            private String type;
            private String difficulty;
            private String question;
            private String correct_answer;
            private List<String> incorrect_answers;

            // Getters and Setters
            public String getCategory() { return category; }
            public void setCategory(String category) { this.category = category; }
            public String getType() { return type; }
            public void setType(String type) { this.type = type; }
            public String getDifficulty() { return difficulty; }
            public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
            public String getQuestion() { return question; }
            public void setQuestion(String question) { this.question = question; }
            public String getCorrect_answer() { return correct_answer; }
            public void setCorrect_answer(String correct_answer) { this.correct_answer = correct_answer; }
            public List<String> getIncorrect_answers() { return incorrect_answers; }
            public void setIncorrect_answers(List<String> incorrect_answers) { this.incorrect_answers = incorrect_answers; }
        }
    }
}