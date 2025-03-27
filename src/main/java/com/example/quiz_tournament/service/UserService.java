package com.example.quiz_tournament.service;

import com.example.quiz_tournament.model.User;
import com.example.quiz_tournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.Optional;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(@Valid User user) {
        // Password is validated by @Valid before reaching this point
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validateLogin(String username, String password) {
        Optional<User> userOpt = findByUsername(username);
        return userOpt.isPresent() &&
                passwordEncoder.matches(password, userOpt.get().getPassword());
    }

    public User updateUser(Long id, @Valid User newUserData) {
        return userRepository.findById(id)
                .map(user -> {
                    // Don't update password here - use separate endpoint for password changes
                    user.setUsername(newUserData.getUsername());
                    user.setFirstName(newUserData.getFirstName());
                    user.setLastName(newUserData.getLastName());
                    user.setEmail(newUserData.getEmail());
                    user.setPhoneNumber(newUserData.getPhoneNumber());
                    user.setAddress(newUserData.getAddress());
                    user.setProfilePicture(newUserData.getProfilePicture());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}