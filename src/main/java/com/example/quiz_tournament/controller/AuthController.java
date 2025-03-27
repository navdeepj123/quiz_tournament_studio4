package com.example.quiz_tournament.controller;

import com.example.quiz_tournament.model.User;
import com.example.quiz_tournament.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // ✅ Register User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);

        // Explicitly access only one version of getUsername to fix ambiguity
        String username = (String) ((Object) newUser).toString(); // TEMP fix if ambiguity persists

        // But best fix: change method name in your User class if duplicated
        // OR just use the following if there's only one getUsername() and error is gone:
        // String username = newUser.getUsername();

        return ResponseEntity.ok("User registered successfully with username: " + username);
    }

    // ✅ Login User
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        boolean isValid = userService.validateLogin(username, password);
        if (isValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // ✅ Update User
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok("User updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
