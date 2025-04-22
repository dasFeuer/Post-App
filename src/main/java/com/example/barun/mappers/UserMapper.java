package com.example.barun.mappers;

import com.example.barun.domain.dtos.LoginUserDto;
import com.example.barun.domain.dtos.RegisterUserDto;
import com.example.barun.domain.dtos.UserDto;
import com.example.barun.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    RegisterUserDto toRegisterUserDto(User user);

    LoginUserDto toLoginUserDto(User user);
}
