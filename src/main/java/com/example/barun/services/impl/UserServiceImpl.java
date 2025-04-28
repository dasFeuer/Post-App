package com.example.barun.services.impl;

import com.example.barun.domain.PatchUserDataRequest;
import com.example.barun.domain.UpdateUserDataRequest;
import com.example.barun.domain.dtos.AuthResponse;
import com.example.barun.domain.dtos.LoginUserRequest;
import com.example.barun.domain.RegisterUserRequest;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.UserRepository;
import com.example.barun.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Override
    @Transactional
    public User registerTheUser(RegisterUserRequest registerUserRequest) {
        try{
            User newUser = new User();
            newUser.setFullName(registerUserRequest.getFullName());
            newUser.setUsername(registerUserRequest.getUsername());
            newUser.setEmail(registerUserRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
            return userRepository.save(newUser);
        } catch (Exception e){
            throw new RuntimeException("User valid input! " + e);
        }
    }

//    @Override
//    public String verifyTheUser(LoginUserRequest loginUserRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    loginUserRequest.getUsername(),
//                    loginUserRequest.getPassword()
//            ));
//            if (authentication.isAuthenticated()) {
//                String tokenValue = jwtService.generateToken(loginUserRequest.getUsername());
//                AuthResponse authResponse = new AuthResponse();
//                authResponse.setToken(tokenValue);
//                authResponse.setExpiresIn(86400);
//                return "Token: " + authResponse.getToken() + "\n" +
//                        "Expires in: " + authResponse.getExpiresIn() + " seconds";
//            } else {
//                return "Fail!";
//            }
//        } catch (Exception e) {
//            throw new UsernameNotFoundException("User valid input! " + e);
//        }
//    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @Override
//    public void saveUser(User user) {
//        userRepository.save(user);
//    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateOrAddTheImage(Long userId, MultipartFile imageFile) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setImageData(imageFile.getBytes());
            user.setImageType(imageFile.getContentType());
            user.setImageName(imageFile.getOriginalFilename());
            return userRepository.save(user);
        } else {
            throw new IOException("User not found!");
        }
    }

    @Override
    public User updateImage(Long userId, MultipartFile imageFile) throws IOException {
        return updateOrAddTheImage(userId, imageFile);
    }

    @Override
    public User addUserProfileImage(Long userId, MultipartFile imageFile) throws IOException {
        return updateOrAddTheImage(userId, imageFile);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User patchUserInfo(Long id, PatchUserDataRequest patchUserDataRequest) throws IOException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updateUser = existingUser.get();
            if (patchUserDataRequest.getUsername() != null) {
                updateUser.setUsername(patchUserDataRequest.getUsername());
            }
            if (patchUserDataRequest.getEmail() != null) {
                updateUser.setEmail(patchUserDataRequest.getEmail());
            }
            if (patchUserDataRequest.getFullName() != null) {
                updateUser.setFullName(patchUserDataRequest.getFullName());
            }
            if (patchUserDataRequest.getPassword() != null) {
                updateUser.setPassword(passwordEncoder.encode(patchUserDataRequest.getPassword()));
            }
            return userRepository.save(updateUser);
        } else {
            throw new IOException("User not found!");
        }
    }

    @Override
    public User updateUserInfo(Long id, UpdateUserDataRequest updateUserDataRequest) throws IOException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(updateUserDataRequest.getUsername());
            updatedUser.setEmail(updateUserDataRequest.getEmail());
            updatedUser.setFullName(updateUserDataRequest.getFullName());
            updatedUser.setPassword(passwordEncoder.encode(updateUserDataRequest.getPassword()));
            return userRepository.save(updatedUser);
        } else {
            throw new IOException("User not found!");
        }
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserImageById(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User userImage = existingUser.get();
            userImage.setImageData(null);
            userImage.setImageType(null);
            userImage.setImageName(null);
            userRepository.save(userImage);
        } else {
            throw new RuntimeException("User Image not found!");
        }
    }

}