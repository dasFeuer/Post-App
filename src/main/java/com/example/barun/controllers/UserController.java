package com.example.barun.controllers;

import com.example.barun.dto.LoginUserDto;
import com.example.barun.dto.RegisterUserDto;
import com.example.barun.entities.userEntities.User;
import com.example.barun.services.JwtService;
import com.example.barun.services.UserDetailsServiceImplementation;
import com.example.barun.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    private ResponseEntity<String> unauthorized(){
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    private ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto registerUserDto){
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
    private ResponseEntity<String> login(@Valid @RequestBody LoginUserDto loginUserDto){
        String userJwt = userService.verifyTheUser(loginUserDto);
        if ("Fail!".equals(userJwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed!");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userJwt);
    }

    @GetMapping("/allUsers")
    private ResponseEntity<List<User>> getAllUsers(){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
           List<User> allUsers = userService.getAllUsers();
            return ResponseEntity.ok(allUsers);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<User> getUserById(@PathVariable Long id){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            try{
                User user = userService.getUserById(id);
                // Ternary operator-> conditions ? valueIfTure : valueIfFalse --> Can be used to handling the null check
                return user != null
                        ? ResponseEntity.ok(user)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                if(user != null){
//                    return new ResponseEntity<>(user, HttpStatus.FOUND);
//                } else {
//                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    @PostMapping("/{id}/addImage")
    public ResponseEntity<?> addUserImage(@PathVariable("id") Long userId,
                                           @RequestPart("file")MultipartFile multipartFile) {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return unauthorized();
        }

        if(!loggedInUser.getId().equals(userId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            User theUser = userService.addImage(userId, multipartFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(theUser);
        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
            }
        }

    @PutMapping("/{id}/updateImage")
    private ResponseEntity<?> updateImage(@PathVariable("id") Long userId,
                                           @RequestPart("file")MultipartFile multipartFile) {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

            try {
                User theUser = userService.updateImage(userId, multipartFile);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(theUser);
            } catch (Exception exception){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
            }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImageByUserId(@PathVariable Long id){
        try{
            User user = userService.getUserById(id);
            if(user == null || user.getImageData() == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            byte[] imageFile = user.getImageData(); // --> Not Optional<User> = user.getImageData()
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(user.getImageType()))
                    .body(imageFile);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/{id}/patchData")
    public ResponseEntity<User> patchUserData(@PathVariable Long id, @RequestBody User user) throws IOException {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.getId().equals(id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
                User updatedUser = userService.patchUserInfo(id, user);
                return ResponseEntity.ok().body(updatedUser);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @PutMapping("/{id}/updateData")
    public ResponseEntity<User> updateData(@PathVariable Long id, @RequestBody User user) throws IOException {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!loggedInUser.getId().equals(id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
            try{
                User updatedUser = userService.updateUserInfo(id, user);
                return ResponseEntity.ok().body(updatedUser);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    @DeleteMapping("/{id}/deleteUser")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.getId().equals(id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

            try{
                userService.deleteUser(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @GetMapping("/{userUsername}/username")
    public ResponseEntity<User> getByUsername(@PathVariable String userUsername){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            try{
                User userByUsername = userService.getUserByUsername(userUsername);
//                return new ResponseEntity<>(userByUsername, HttpStatus.OK);
                return userByUsername != null
                        ? ResponseEntity.ok(userByUsername)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
