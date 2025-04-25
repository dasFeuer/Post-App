package com.example.barun.mappers;

import com.example.barun.domain.RegisterUserRequest;
import com.example.barun.domain.UpdateUserDataRequest;
import com.example.barun.domain.dtos.RegisterUserRequestDto;
import com.example.barun.domain.dtos.UpdateUserDataRequestDto;
import com.example.barun.domain.dtos.UserDto;
import com.example.barun.domain.entities.User;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    RegisterUserRequest toRegisterUserRequest(RegisterUserRequestDto registerUserRequestDto);

    UpdateUserDataRequest toUpdateUserDataRequest(UpdateUserDataRequestDto updateUserDataRequestDto);
}
