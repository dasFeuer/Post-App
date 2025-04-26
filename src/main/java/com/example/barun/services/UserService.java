package com.example.barun.services;

import com.example.barun.domain.PatchUserDataRequest;
import com.example.barun.domain.UpdateUserDataRequest;
import com.example.barun.domain.RegisterUserRequest;
import com.example.barun.domain.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    User registerTheUser(RegisterUserRequest registerUserRequest);

//    String verifyTheUser(LoginUserRequest loginUserRequest);

    List<User> getAllUsers();

//    void saveUser(User user);

    User getUserByUsername(String username);

    User updateOrAddTheImage(Long userId, MultipartFile imageFile) throws IOException;

    User updateImage(Long userId, MultipartFile imageFile) throws IOException;

    User addUserProfileImage(Long userId, MultipartFile imageFile) throws IOException;

    User getUserById(Long id);

    User patchUserInfo(Long id, PatchUserDataRequest patchUserDataRequest) throws IOException;

    User updateUserInfo(Long id, UpdateUserDataRequest updateUserDataRequest) throws IOException;

    void deleteUser(Long id);
}
