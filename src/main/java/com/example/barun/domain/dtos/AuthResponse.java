package com.example.barun.domain.dtos;

import java.util.Objects;

public class AuthResponse {
    private String token;
    private long expiresIn;

    public AuthResponse() {}

    public AuthResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, expiresIn);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
