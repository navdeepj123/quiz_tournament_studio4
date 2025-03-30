// src/main/java/com/example/quiz_tournament/payload/request/AdminCreateRequest.java
package com.example.quiz_tournament.payload.request;

import jakarta.validation.constraints.*;

public class AdminCreateRequest {
    @NotBlank @Size(min = 3, max = 20)
    private String username;

    @NotBlank @Size(max = 50) @Email
    private String email;

    @NotBlank @Size(min = 6, max = 40)
    private String password;

    @NotBlank @Size(max = 50)
    private String firstName;

    @NotBlank @Size(max = 50)
    private String lastName;

    private String profileImageUrl; // Optional

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}