package com.example.barun.controllers;

import com.example.barun.domain.PatchUserDataRequest;
import com.example.barun.domain.UpdateUserDataRequest;
import com.example.barun.domain.UserSummaryDto;
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
import java.util.Optional;

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

    private Optional<User> getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.getUserByEmail(email);
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

    @PostMapping("/login") // --> For this: Service/ Business logic not needed
    private ResponseEntity<AuthResponse> loginUser(@RequestBody LoginUserRequest loginUserRequest){
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(
                loginUserRequest.getEmail(),
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
    private ResponseEntity<List<UserSummaryDto>> getAllUsers(){
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
           List<User> allUsers = userService.getAllUsers();
           List<UserSummaryDto> userDtos = allUsers.stream().map(userMapper::toSummaryDto).toList();
            return ResponseEntity.ok(userDtos);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserSummaryDto> getUserById(@PathVariable Long id){
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            try{
                User user = userService.getUserById(id);
                UserSummaryDto userSummaryDto = userMapper.toSummaryDto(user);
                // Ternary operator-> conditions ? valueIfTure : valueIfFalse --> Can be used to handling the null check
                return user != null
                        ? ResponseEntity.ok(userSummaryDto)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    @PostMapping("/{id}/addImage")
    public ResponseEntity<?> addUserImage(
            @PathVariable("id") Long userId,
            @RequestPart("file") MultipartFile multipartFile) {
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.get().getId().equals(userId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            // Check if the user already had an image file exists
            if (loggedInUser.get().getImageData() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Image already present, use update image request!");
            }
            User theUser = userService.addUserProfileImage(userId, multipartFile);
            UserProfileResponseDto userProfileResponseDto = userMapper.toProfileResponseDto(theUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userProfileResponseDto);
        } catch (IOException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    @PutMapping("/{id}/updateImage")
    private ResponseEntity<?> updateImage(@PathVariable("id") Long userId,
                                           @RequestPart("file")MultipartFile multipartFile) {
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.get().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

            try {
                if (loggedInUser.get().getImageData() == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("First add/upload the image to update it");
                }
                User theUser = userService.updateImage(userId, multipartFile);
                UserProfileResponseDto userProfileResponseDto = userMapper.toProfileResponseDto(theUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(userProfileResponseDto);
            } catch (Exception exception){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<UserDto> patchUserData(@PathVariable Long id,
                                                 @RequestBody PatchUserDataRequestDto patchUserDataRequestDto) throws IOException {
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.get().getId().equals(id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            PatchUserDataRequest patchUserDataRequest = userMapper.toPatchUserDataRequest(patchUserDataRequestDto);
                User updatedUser = userService.patchUserInfo(id, patchUserDataRequest);
                UserDto userDto = userMapper.toDto(updatedUser);
                return ResponseEntity.ok().body(userDto);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @PutMapping("/{id}/updateData")
    public ResponseEntity<UserDto> updateUserData(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateUserDataRequestDto updateUserDataRequestDto)
            throws IOException {
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!loggedInUser.get().getId().equals(id)){
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
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.get().getId().equals(id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

            try{
                userService.deleteUser(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @DeleteMapping("/{id}/deleteUserImage")
    public ResponseEntity<Void> deleteUserImage(@PathVariable Long id){
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.get().getId().equals(id)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

            try{
                userService.deleteUserImageById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @GetMapping("/{userUsername}/username")
    public ResponseEntity<UserSummaryDto> getByUsername(@PathVariable String userUsername){
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            try{
                User user = userService.getUserByUsername(userUsername);
                UserSummaryDto userByUsername = userMapper.toSummaryDto(user);
                return ResponseEntity.ok(userByUsername);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    @GetMapping("/email")
    public ResponseEntity<UserSummaryDto> getByEmail(@RequestParam("email") String email){
        Optional<User> loggedInUser = getAuthenticatedUser();
        if(loggedInUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!loggedInUser.get().getEmail().equals(email)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
            try{
                Optional<User> user = userService.getUserByEmail(email);
                if(user.isEmpty()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                UserSummaryDto userByEmail = userMapper.toSummaryDto(user.get());
                return ResponseEntity.ok(userByEmail);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

}
