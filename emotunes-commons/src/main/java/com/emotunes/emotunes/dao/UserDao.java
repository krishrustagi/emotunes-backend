package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.mapper.UserMapper;
import com.emotunes.emotunes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final UserRepository userRepository;

    public String saveAndGetModelId(UserDto userDto) {
        StoredUser user = UserMapper.toEntity(userDto);
        userRepository.save(user);
        return user.getTrainingModelId();
    }

    public StoredUser findByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public List<StoredUser> findAll() {
        return userRepository.findAll();
    }
}
