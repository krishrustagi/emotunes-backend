package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public StoredUser toEntity(UserDto userDto) {
        return StoredUser.builder()
                .id(IdGenerationUtil.getRandomId())
                .emailId(userDto.getEmailId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();
    }
}
