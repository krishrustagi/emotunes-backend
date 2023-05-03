package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.mapper.UserMapper;
import com.emotunes.emotunes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final UserRepository userRepository;

    public void save(UserDto userDto) {
        StoredUser user = UserMapper.toEntity(userDto);
        userRepository.save(user);
    }

    public StoredUser findByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public List<StoredUser> findAll() {
        return userRepository.findAll();
    }

    public List<String> getModelWeightsUrls(List<String> userIdList) {
        return userRepository.getModelWeightsUrl(userIdList);
    }

    @Transactional
    public void updateModelWeightsUrlsByUserIds(List<String> userIdList, List<String> newModelWeightsUrlList) {
        userRepository.updateModelWeightsUrlsByUserIds(userIdList, newModelWeightsUrlList);
    }
}
