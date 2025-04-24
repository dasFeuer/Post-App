package com.example.barun.mappers;

import com.example.barun.domain.RegisterUserRequest;
import com.example.barun.domain.dtos.UserDto;
import com.example.barun.domain.entities.User;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
// Todo : For mappers new to create new file i.e. RegisterUserRequestDto
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    RegisterUserRequest toRegisterUserDto(@Valid RegisterUserRequest registerUserRequestDto);

}
