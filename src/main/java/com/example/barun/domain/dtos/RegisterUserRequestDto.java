package com.example.barun.domain.dtos;

import jakarta.validation.constraints.NotNull;

public class RegisterUserRequestDto {
    @NotNull(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    public RegisterUserRequestDto(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public @NotNull(message = "Full name is required") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotNull(message = "Full name is required") String fullName) {
        this.fullName = fullName;
    }

    public @NotNull(message = "Username is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull(message = "Username is required") String username) {
        this.username = username;
    }

    public @NotNull(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotNull(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password is required") String password) {
        this.password = password;
    }
}
