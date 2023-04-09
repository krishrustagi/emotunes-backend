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
import java.util.Collections;
import java.util.List;

import static java.util.Collections.shuffle;

@Component
@RequiredArgsConstructor
public class UserSongMappingDao {

    private final SongRepository songRepository;
    private final UserSongMappingRepository userSongMappingRepository;

    public void addMapping(String userId, String songId, Emotion emotion) {
        userSongMappingRepository.save(UserSongMappingMapper.toEntity(userId, songId, emotion));
    }

    public List<SongMetadata> getPaginatedAllSongsForUser(String userId, String songId, int pageSize) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findPaginatedAllSongsForUser(userId, songId, pageSize);
        return getAndShuffleSongMetaDataList(userSongMappingList);
    }

    public List<SongMetadata> getSongsByPrefix(String userId, String prefix) {
        // todo: find using edit distance/trie
        return null;
    }

    public List<SongMetadata> getPaginatedSongsByEmotionForUser(String userId, String songId, Emotion emotion, int pageSize) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findPaginatedSongsByEmotionForUser(userId, songId, emotion.name(), pageSize);

        return getAndShuffleSongMetaDataList(userSongMappingList);
    }

    public List<SongMetadata> getPaginatedLikedSongs(String userId, String songId, int pageSize) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findPaginatedLikedSongsOfUser(userId, songId, pageSize);

        return getAndShuffleSongMetaDataList(userSongMappingList);
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

    private List<SongMetadata> getAndShuffleSongMetaDataList(List<StoredUserSongMapping> userSongMappingList) {
        List<SongMetadata> songMetadataList = generateSongMetadataList(userSongMappingList);
        Collections.shuffle(songMetadataList);
        return songMetadataList;
    }

    @Transactional
    public void toggleLike(String userId, String songId) {
        userSongMappingRepository.toggleLike(userId, songId);
    }

    @Transactional
    public void updateSongEmotionForUser(String userId, String songId, Emotion correctEmotion) {
        userSongMappingRepository.updateSongEmotionForUser(userId, songId, String.valueOf(correctEmotion));
    }
}
