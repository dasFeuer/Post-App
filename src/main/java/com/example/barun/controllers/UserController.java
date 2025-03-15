package com.example.barun.controllers;

import com.example.barun.dto.LoginUserDto;
import com.example.barun.dto.RegisterUserDto;
import com.example.barun.enitities.User;
import com.example.barun.services.JwtService;
import com.example.barun.services.UserDetailsServiceImplementation;
import com.example.barun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController{

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @PostMapping("/register")
    private ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto){
        User newUser = userService.registerTheUser(registerUserDto);
        System.out.println("User registered!");
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

//    @PostMapping("/login") --> For this: Service/ Business logic not needed
//    private ResponseEntity<String> login(@RequestBody LoginUserDto loginUserDto){
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                loginUserDto.getUsername(),
//                loginUserDto.getPassword()
//        ));
//        UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(loginUserDto.getUsername());
//        String jwt = jwtService.generateToken(userDetails.getUsername());
//        System.out.println("User logged in!");
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwt);
//    }

    @PostMapping("/login") // --> For this: Service/ Business logic needed (UserService.java)
    private ResponseEntity<String> login(@RequestBody LoginUserDto loginUserDto){
        String user = userService.verifyTheUser(loginUserDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }

    @GetMapping("/allUsers")
    private ResponseEntity<List<User>> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}")
    private ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/addImage")
    private ResponseEntity<?> addUserImage(@PathVariable("id") Long userId,
                                           @RequestPart("file")MultipartFile multipartFile) {
        try {
            User theUser = userService.addImage(userId, multipartFile);
            return new ResponseEntity<>(theUser, HttpStatus.CREATED);
        } catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/image")
    private ResponseEntity<byte[]> getImageByUserId(@PathVariable Long id){
        User user = userService.getUserById(id);
        byte[] imageFile = user.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(user.getImageType())).body(imageFile);
    }
}
