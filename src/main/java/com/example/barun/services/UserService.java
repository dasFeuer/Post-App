package com.example.barun.services;

import com.example.barun.enitities.User;
import com.example.barun.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public void allUser(){
//        userRepository.findAll();
//    }
    public List<User> allUser(){
        return new ArrayList<>(userRepository.findAll());
    }
}
