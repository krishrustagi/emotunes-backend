package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongEmotionMappingDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.mapper.UserMapper;
import com.emotunes.emotunes.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final SongsDao songsDao;
    private final UserDao userDao;
    private final UserSongEmotionMappingDao userSongEmotionMappingDao;

    @Override
    public ResponseEntity<String> addSong(MultipartFile songFile) throws IOException {

        File file = convert(songFile);

        try {
            AudioFile audioFile = AudioFileIO.read(file);
            long duration = audioFile.getAudioHeader().getTrackLength();
            log.info("{}", Instant.ofEpochSecond(duration).atZone(
                    ZoneId.of("UTC")
            ).toLocalTime().toString());

            SongMetadata songMetadata =
                    SongMetadata.builder()
                            .title(songFile.getOriginalFilename())
                            .duration(Instant.ofEpochSecond(duration).atZone(
                                    ZoneId.of("UTC")
                            ).toLocalTime().toString())
                            .build();

            String songId = persistSong(songMetadata);

            List<StoredUser> userList = userDao.findAll();
            userList.forEach(
                    user -> {
                        // todo: predict song by model id (userId);
                        persistUserSongEmotionMapping(user.getId(), songId, Emotion.HAPPY);
                    }
            );

        } catch (Exception e) {
            log.info("Error while getting audio details! ", e);
            return ResponseEntity.internalServerError().body("Song processing failed!");
        } finally {
            Files.delete(file.toPath());
        }

        return ResponseEntity.ok().body("Song processing triggered successfully!");
    }

    @Override
    public void registerUser(UserDto userDto) {
        if (Objects.isNull(userDao.findByEmailId(userDto.getEmailId()))) {
            userDao.save(UserMapper.toEntity(userDto));
            // todo: add model space and rename model as the userId
        }
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }

        return convFile;
    }

    private String persistSong(SongMetadata songMetadata) {
        // todo: save file with the song id;
        return songsDao.addSong(songMetadata);
    }

    private void persistUserSongEmotionMapping(
            String userId, String songId, Emotion emotion) {
        userSongEmotionMappingDao.addSong(userId, songId, emotion);
    }
}
