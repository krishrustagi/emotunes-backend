package com.emotunes.emotunes.mapper;

import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.experimental.UtilityClass;

import static com.emotunes.emotunes.constants.StringConstants.UNDERSCORE;

@UtilityClass
public class UserMapper {

    public StoredUser toEntity(UserDto userDto) {
        return StoredUser.builder()
                .id(userDto.getUserId())
                .emailId(userDto.getEmailId())
                .name(userDto.getName())
                .trainingModelId(userDto.getUserId() + UNDERSCORE + IdGenerationUtil.getRandomId())
                .build();
    }
}
