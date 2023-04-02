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
import org.springframework.transaction.annotation.Transactional;

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
        userSongMappingRepository.save(UserSongMappingMapper.toEntity(userId, songId, emotion));
    }

    public List<SongMetadata> getAll(String userId) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findAllSongsOfUser(userId);

        List<SongMetadata> songMetadataList = new ArrayList<>();

        userSongMappingList.forEach(userSongMapping -> {
            StoredSong song = songRepository.getReferenceById(userSongMapping.getSongId());
            songMetadataList.add(SongMapper.toSongMetadata(song, userSongMapping.getEmotion(),
                    userSongMapping.isLiked()));
        });
        return songMetadataList;
    }

    public List<SongMetadata> getSongsByPrefix(String userId, String prefix) {
        // todo: find using edit distance/trie
        return null;
    }

    public List<SongMetadata> getSongsByEmotion(String userId, Emotion emotion) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findAllSongsWithEmotionOfUser(userId, emotion.name());

        List<SongMetadata> songMetadataList = new ArrayList<>();

        userSongMappingList.forEach(userSongMapping -> {
            StoredSong song = songRepository.getReferenceById(userSongMapping.getSongId());
            songMetadataList.add(SongMapper.toSongMetadata(song, emotion,
                    userSongMapping.isLiked()));
        });

        return songMetadataList;
    }

    public List<SongMetadata> getAllLikedSongs(String userId) {
        List<StoredUserSongMapping> userSongMappingList =
                userSongMappingRepository.findAllLikedSongsOfUser(userId);

        List<SongMetadata> songMetadataList = new ArrayList<>();

        userSongMappingList.forEach(userSongMapping -> {
            StoredSong song = songRepository.getReferenceById(userSongMapping.getSongId());
            songMetadataList.add(SongMapper.toSongMetadata(song, userSongMapping.getEmotion(),
                    true));
        });

        return songMetadataList;
    }

    @Transactional
    public void songLiked(String userId, SongMetadata songMetadata, boolean isLiked) {
        String songId = songRepository.getIdByTitleAndDuration(songMetadata.getTitle(),
                LocalTime.parse(songMetadata.getDuration()));

        userSongMappingRepository.updateSongToLikedForUser(userId, songId, isLiked);
    }

    public void addSongsForUser(String userId) {
        List<StoredSong> songList = songRepository.findAll();
        songList.forEach(song -> {
            Emotion songEmotion = Emotion.HAPPY; // todo: use pre-defined emotions the song
            addSong(userId, song.getId(), songEmotion);
        });
    }
}
