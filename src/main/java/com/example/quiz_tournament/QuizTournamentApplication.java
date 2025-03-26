package com.example.quiz_tournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QuizTournamentApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizTournamentApplication.class, args);
	}

	// ✅ RestTemplate Bean to call external APIs
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
