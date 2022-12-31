package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.entity.StoredDislikedSong;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.repository.DislikedSongRepository;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DislikedSongDao {

    private final DislikedSongRepository dislikedSongRepository;

    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public void saveDislikedSong(String userId, String songId, Emotion correctEmotion) {
        dislikedSongRepository.save(
                StoredDislikedSong.builder()
                        .user(userRepository.getReferenceById(userId))
                        .song(songRepository.getReferenceById(songId))
                        .correctEmotion(correctEmotion)
                        .build());
    }

}
