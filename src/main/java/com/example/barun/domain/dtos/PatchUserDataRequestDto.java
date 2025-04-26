package com.example.barun.domain.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatchUserDataRequestDto {
    @NotNull(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Username is required")
    @Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters")
    private String username;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    public PatchUserDataRequestDto() {}

    public PatchUserDataRequestDto(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
