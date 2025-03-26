package com.example.quiz_tournament.service;  // Make sure this is the correct package

import com.example.quiz_tournament.model.User;
import com.example.quiz_tournament.repository.UserRepository;  // Correct repository import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public User registerUser(User user) {
        user.setPassword(hashPassword(user.getPassword())); // Hash password
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validateLogin(String username, String password) {
        Optional<User> userOpt = findByUsername(username);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(hashPassword(password));
    }

    public User updateUser(Long id, User newUserData) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(newUserData.getUsername());
            user.setFirstName(newUserData.getFirstName());
            user.setLastName(newUserData.getLastName());
            user.setEmail(newUserData.getEmail());
            user.setPhoneNumber(newUserData.getPhoneNumber());
            user.setAddress(newUserData.getAddress());
            user.setProfilePicture(newUserData.getProfilePicture());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
