package com.example.barun.services;

import com.example.barun.dto.LoginUserDto;
import com.example.barun.dto.RegisterUserDto;
import com.example.barun.enitities.User;
import com.example.barun.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public User registerTheUser(RegisterUserDto registerUserDto){
        User newUser = new User();
        newUser.setFullName(registerUserDto.getFullName());
        newUser.setUsername(registerUserDto.getUsername());
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        return userRepository.save(newUser);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public String verifyTheUser(LoginUserDto user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        ));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        } else {
            return "Fail!";
        }
    }

    public User addImage(Long userId, MultipartFile imageFile) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setImageData(imageFile.getBytes());
            user.setImageType(imageFile.getContentType());
            user.setImageName(imageFile.getOriginalFilename());
            return userRepository.save(user);
        }else {
            throw new IOException("User not found!");
        }
    }

    public User getUserById(Long id) {
         return userRepository.findById(id).orElse(null);
    }

}
