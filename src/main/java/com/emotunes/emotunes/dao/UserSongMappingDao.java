package com.emotunes.emotunes.dao;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUserSongMapping;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.SongMapper;
import com.emotunes.emotunes.mapper.UserSongMappingMapper;
import com.emotunes.emotunes.repository.SongRepository;
import com.emotunes.emotunes.repository.UserRepository;
import com.emotunes.emotunes.repository.UserSongMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSongMappingDao {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final UserSongMappingRepository userSongMappingRepository;

    public void addSong(String userId, String songId, Emotion emotion) {
        userSongMappingRepository.save(
                UserSongMappingMapper.toEntity(userRepository.getReferenceById(userId),
                        songRepository.getReferenceById(songId), emotion));
    }

    public List<SongMetadata> getAll(String userId) {
        List<StoredUserSongMapping> songList =
                userSongMappingRepository.findAllSongsOfUser(userRepository.getReferenceById(userId));

        List<SongMetadata> songMetadataList = new ArrayList<>();

        songList.forEach(song -> songMetadataList.add(SongMapper.toSongMetadata(song)));
        return songMetadataList;
    }

    public List<SongMetadata> getSongsByPrefix(String userId, String prefix) {
        List<StoredUserSongMapping> songList =
                userSongMappingRepository.findAllSongsOfUser(userRepository.getReferenceById(userId));

        // todo: changes for finding using prefix
        List<SongMetadata> songMetadataList = new ArrayList<>();

        songList.forEach(song -> songMetadataList.add(SongMapper.toSongMetadata(song)));
        return songMetadataList;
    }

    public List<SongMetadata> getSongsByEmotion(String userId, Emotion emotion) {
        List<StoredUserSongMapping> songList =
                userSongMappingRepository.findAllSongsWithEmotionOfUser(userRepository.getReferenceById(userId),
                        emotion.name());

        List<SongMetadata> songMetadataList = new ArrayList<>();

        songList.forEach(song -> songMetadataList.add(SongMapper.toSongMetadata(song)));
        return songMetadataList;
    }

    public List<SongMetadata> getAllLikedSongs(String userId) {
        List<StoredUserSongMapping> songList =
                userSongMappingRepository.findAllLikedSongsOfUser(userRepository.getReferenceById(userId));

        List<SongMetadata> songMetadataList = new ArrayList<>();

        songList.forEach(song -> songMetadataList.add(SongMapper.toSongMetadata(song)));
        return songMetadataList;
    }

    public void songLiked(String userId, SongMetadata songMetadata) {
        StoredSong song = songRepository.getByTitleAndDuration(songMetadata.getTitle(),
                LocalTime.parse(songMetadata.getDuration()));

        userSongMappingRepository.updateSongToLikedForUser(userRepository.getReferenceById(userId), song);
    }
}
