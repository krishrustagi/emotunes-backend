package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.SongAsPerEmotionMapper;
import com.emotunes.emotunes.repository.SongAsPerEmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SongAsPerEmotionDao {

    private final SongAsPerEmotionRepository songAsPerEmotionRepository;

    public void save(String userId, SongMetadata songMetadata, Emotion correctEmotion) {
        songAsPerEmotionRepository.save(
                SongAsPerEmotionMapper.toEntity(userId, songMetadata.getSongId(), correctEmotion));
    }

}
