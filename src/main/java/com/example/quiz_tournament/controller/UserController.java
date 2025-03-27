package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.User;
import com.example.quiz_tournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ✅ View all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Create a user (admin/player)
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // ✅ Delete user by ID (with validation)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // ✅ Promote user to admin
    @PutMapping("/{id}/promote")
    public ResponseEntity<?> promoteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole("admin");
            userRepository.save(user);
            return ResponseEntity.ok("User promoted to admin");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // ✅ Create Admin User
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdminUser(@RequestBody User user) {
        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role must be ADMIN");
        }

        // Optional: Check if admin already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok("Admin user created successfully!");
    }
}
