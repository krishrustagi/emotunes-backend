package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUserSongMapping;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.mapper.UserSongMappingMapper;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserSongMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSongMappingDao {

    private final SongRepository songRepository;
    private final UserSongMappingRepository userSongMappingRepository;

    public void addMapping(String userId, String songId, Emotion emotion) {
        userSongMappingRepository.save(UserSongMappingMapper.toEntity(userId, songId, emotion));
    }

    public List<SongMetadata> getAllSongs(String userId) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findAllSongsOfUser(userId);

        return generateSongMetadataList(userSongMappingList);
    }

    public List<SongMetadata> getSongsByPrefix(String userId, String prefix) {
        // todo: find using edit distance/trie
        return null;
    }

    public List<SongMetadata> getSongsByEmotion(String userId, Emotion emotion) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findAllSongsWithEmotionOfUser(userId, emotion.name());

        return generateSongMetadataList(userSongMappingList);
    }

    public List<SongMetadata> getAllLikedSongs(String userId) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findAllLikedSongsOfUser(userId);

        return generateSongMetadataList(userSongMappingList);
    }

    private List<SongMetadata> generateSongMetadataList(List<StoredUserSongMapping> userSongMappingList) {
        List<SongMetadata> songMetadataList = new ArrayList<>();

        userSongMappingList.forEach(userSongMapping -> {
            StoredSong song = songRepository.getReferenceById(userSongMapping.getSongId());
            songMetadataList.add(SongMapper.toSongMetadata(song, userSongMapping.getEmotion(),
                    userSongMapping.isLiked()));
        });
        return songMetadataList;
    }

    @Transactional
    public void toggleLike(String userId, String songId) {
        userSongMappingRepository.toggleLike(userId, songId);
    }
}
