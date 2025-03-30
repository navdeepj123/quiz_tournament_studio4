package com.example.quiz_tournament;

import com.example.quiz_tournament.model.ERole;
import com.example.quiz_tournament.model.Role;
import com.example.quiz_tournament.model.User;
import com.example.quiz_tournament.repository.RoleRepository;
import com.example.quiz_tournament.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QuizTournamentApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizTournamentApplication.class, args);
	}

	@Configuration
	public class AppConfig {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository,
						   UserRepository userRepository,
						   PasswordEncoder passwordEncoder) {
		return args -> {
			// Create roles if they don't exist
			if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_ADMIN));
			}
			if (roleRepository.findByName(ERole.ROLE_PLAYER).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_PLAYER));
			}

			// Create default admin if not exists
			if (!userRepository.existsByUsername("admin")) {
				User admin = new User(
						"admin",
						"admin@quiztournament.com",
						passwordEncoder.encode("P@ss123"),
						"System",
						"Administrator"
				);

				Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Admin role not found"));
				admin.getRoles().add(adminRole);

				userRepository.save(admin);
				System.out.println("Default admin user created successfully!");
			}
		};
	}
}