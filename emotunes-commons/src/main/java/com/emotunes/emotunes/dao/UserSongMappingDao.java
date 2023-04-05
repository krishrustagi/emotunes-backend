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

    public void addSong(String userId, String songId, Emotion emotion) {
        userSongMappingRepository.save(UserSongMappingMapper.toEntity(userId, songId, emotion));
    }

    public List<SongMetadata> getPaginatedAllSongsForUser(String userId, String songId, int pageSize) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findPaginatedAllSongsForUser(userId, songId, pageSize);

        return generateSongMetadataList(userSongMappingList);
    }

    public List<SongMetadata> getSongsByPrefix(String userId, String prefix) {
        // todo: find using edit distance/trie
        return null;
    }

    public List<SongMetadata> getPaginatedSongsByEmotionForUser(String userId, String songId, Emotion emotion, int pageSize) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findPaginatedSongsWithEmotionForUser(userId, songId, emotion.name(), pageSize);

        return generateSongMetadataList(userSongMappingList);
    }

    public List<SongMetadata> getPaginatedLikedSongs(String userId, String songId, int pageSize) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findPaginatedLikedSongsOfUser(userId, songId, pageSize);

        return generateSongMetadataList(userSongMappingList);
    }

    public void addSongsForUser(String userId) {
        List<StoredSong> songList = songRepository.findAll();
        songList.forEach(song -> {
            Emotion songEmotion = Emotion.HAPPY; // todo: use pre-defined emotions the song
            addSong(userId, song.getId(), songEmotion);
        });
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
