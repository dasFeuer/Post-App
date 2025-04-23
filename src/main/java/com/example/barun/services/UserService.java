package com.example.barun.services;

import com.example.barun.domain.dtos.LoginUserDto;
import com.example.barun.domain.dtos.RegisterUserDto;
import com.example.barun.domain.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    User registerTheUser(RegisterUserDto registerUserDto);

    String verifyTheUser(LoginUserDto loginUserDto);

    List<User> getAllUsers();

    void saveUser(User user);

    User getUserByUsername(String username);

    User updateOrAddTheImage(Long userId, MultipartFile imageFile) throws IOException;

    User updateImage(Long userId, MultipartFile imageFile) throws IOException;

    User addImage(Long userId, MultipartFile imageFile) throws IOException;

    User getUserById(Long id);

    User patchUserInfo(Long id, User user) throws IOException;

    User updateUserInfo(Long id, User user) throws IOException;

    void deleteUser(Long id);
}
