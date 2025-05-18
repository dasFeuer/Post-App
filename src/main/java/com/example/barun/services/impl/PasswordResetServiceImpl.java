package com.example.barun.services.impl;

import com.example.barun.domain.PasswordResetToken;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.PasswordResetTokenRepository;
import com.example.barun.repositories.UserRepository;
import com.example.barun.services.EmailService;
import com.example.barun.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void sendResetToken(String email) throws Exception {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(15));
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
        emailService.sendEmail(email, "Password Reset Request",
                "Click the link to reset your password: " + resetLink);
    }

    @Override
    public boolean validateToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(passwordResetToken -> passwordResetToken
                        .getExpiryDate()
                        .isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }
         Optional<User> user = userRepository.findByEmail(passwordResetToken.getEmail());
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }

        tokenRepository.delete(passwordResetToken);
    }
}
