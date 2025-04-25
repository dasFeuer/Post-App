package com.example.barun.controllers;

import com.example.barun.domain.UpdateUserDataRequest;
import com.example.barun.domain.dtos.*;
import com.example.barun.domain.RegisterUserRequest;
import com.example.barun.domain.entities.User;
import com.example.barun.mappers.UserMapper;
import com.example.barun.services.UserService;
import com.example.barun.services.impl.JwtService;
import com.example.barun.services.impl.UserDetailsServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private UserMapper userMapper;;

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    private ResponseEntity<String> unauthorized(){
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    private ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserRequestDto registerUserRequestDto){
        RegisterUserRequest userRequest = userMapper.toRegisterUserRequest(registerUserRequestDto);
        User newUser = userService.registerTheUser(userRequest);
        UserDto registerUserDto = userMapper.toDto(newUser);
        System.out.println("User registered!");
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUserDto);
    }

    @PostMapping("/login") // --> For this: Service/ Business logic
    private ResponseEntity<AuthResponse> loginUser(@RequestBody LoginUserRequest loginUserRequest){
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(
                loginUserRequest.getUsername(),
                loginUserRequest.getPassword()
        )
                );
        String tokenValue = jwtService.generateToken(authentication.getName());
        AuthResponse authResponse = new AuthResponse();
                authResponse.setToken(tokenValue);
                authResponse.setExpiresIn(86400);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authResponse);
    }


//    @PostMapping("/login") // --> For this: Service/ Business logic needed (UserServiceImpl.java)
//    private ResponseEntity<String> login(@Valid @RequestBody LoginUserRequest loginUserRequest){
//        String userJwt = userService.verifyTheUser(loginUserRequest);
//        if ("Fail!".equals(userJwt)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed!");
//        }
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userJwt);
//    }

    @GetMapping("/allUsers")
    private ResponseEntity<List<UserDto>> getAllUsers(){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
           List<User> allUsers = userService.getAllUsers();
           List<UserDto> userDtos = allUsers.stream()
                   .map(user -> userMapper.toDto(user)).toList();
            return ResponseEntity.ok(userDtos);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            try{
                User user = userService.getUserById(id);
                UserDto userDto = userMapper.toDto(user);
                // Ternary operator-> conditions ? valueIfTure : valueIfFalse --> Can be used to handling the null check
                return user != null
                        ? ResponseEntity.ok(userDto)
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
                                           @RequestPart("file") MultipartFile multipartFile) {
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
    public ResponseEntity<UserDto> updateUserData(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateUserDataRequestDto updateUserDataRequestDto)
            throws IOException {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!loggedInUser.getId().equals(id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
            try{
                UpdateUserDataRequest updateUserDataRequest = userMapper.toUpdateUserDataRequest(updateUserDataRequestDto);
                User updatedUser = userService.updateUserInfo(id, updateUserDataRequest);
                UserDto updatedUserDto = userMapper.toDto(updatedUser);
                return ResponseEntity.ok().body(updatedUserDto);
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
    public ResponseEntity<UserDto> getByUsername(@PathVariable String userUsername){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            try{
                User userByUsername = userService.getUserByUsername(userUsername);
                UserDto userByUsernameDto = userMapper.toDto(userByUsername);
//                return new ResponseEntity<>(userByUsername, HttpStatus.OK);
                return userByUsername != null
                        ? ResponseEntity.ok(userByUsernameDto)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
