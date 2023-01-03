package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSongAsPerEmotion;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.repository.SongAsPerEmotionRepository;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@RequiredArgsConstructor
@Component
public class SongAsPerEmotionDao {

    private final SongAsPerEmotionRepository songAsPerEmotionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public void save(
            String userId, SongMetadata songMetadata, Emotion correctEmotion) {
        songAsPerEmotionRepository.save(
                StoredSongAsPerEmotion.builder()
                        .id(IdGenerationUtil.getRandomId())
                        .user(userRepository.getReferenceById(userId))
                        .song(songRepository.getByTitleAndDuration(
                                songMetadata.getTitle(),
                                LocalTime.parse(songMetadata.getDuration())))
                        .correctEmotion(correctEmotion)
                        .build());
    }

}
