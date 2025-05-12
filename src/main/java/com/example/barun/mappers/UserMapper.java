package com.example.barun.mappers;

import com.example.barun.domain.PatchUserDataRequest;
import com.example.barun.domain.RegisterUserRequest;
import com.example.barun.domain.UpdateUserDataRequest;
import com.example.barun.domain.UserSummaryDto;
import com.example.barun.domain.dtos.*;
import com.example.barun.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    UserSummaryDto toSummaryDto(User user);

    UserProfileResponseDto toProfileResponseDto(User user);

    RegisterUserRequest toRegisterUserRequest(RegisterUserRequestDto registerUserRequestDto);

    UpdateUserDataRequest toUpdateUserDataRequest(UpdateUserDataRequestDto updateUserDataRequestDto);

    PatchUserDataRequest toPatchUserDataRequest(PatchUserDataRequestDto patchUserDataRequestDto);
}
