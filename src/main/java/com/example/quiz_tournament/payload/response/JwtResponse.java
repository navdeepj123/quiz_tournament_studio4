package com.example.quiz_tournament.payload.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String phoneNumber;
    private String bio;
    private List<String> roles;

    public JwtResponse(String token, Long id, String username, String email,
                       String firstName, String lastName, String profileImageUrl,
                       String phoneNumber, String bio, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.roles = roles;
    }

    // Getters
    public String getToken() { return token; }
    public String getType() { return type; }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getBio() { return bio; }
    public List<String> getRoles() { return roles; }
}