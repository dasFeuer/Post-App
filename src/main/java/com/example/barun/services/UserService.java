package com.example.barun.services;

import com.example.barun.domain.dtos.LoginUserDto;
import com.example.barun.domain.dtos.RegisterUserDto;
import com.example.barun.domain.entities.User;

public interface UserService {

    User registerTheUser(RegisterUserDto registerUserDto);

    String verifyTheUser(LoginUserDto loginUserDto);
}
