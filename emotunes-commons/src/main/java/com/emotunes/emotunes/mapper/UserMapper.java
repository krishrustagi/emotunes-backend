package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.entity.StoredUser;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public StoredUser toEntity(UserDto userDto) {
        return StoredUser.builder()
                .id(userDto.getUserId())
                .emailId(userDto.getEmailId())
                .name(userDto.getName())
                .modelWeightsUrl(userDto.getModelWeightsUrl())
                .build();
    }
}
