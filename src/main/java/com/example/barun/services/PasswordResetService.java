package com.example.barun.services;

public interface PasswordResetService {
    void sendResetToken(String email) throws Exception;
    boolean validateToken(String token);
    void resetPassword(String token, String newPassword);
}
