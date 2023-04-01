package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
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
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
    private final UserSongMappingDao userSongMappingDao;

    @Override
    public ResponseEntity<String> addSong(MultipartFile songFile) throws IOException {

        File file = convert(songFile);
        file.createNewFile();

        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            String artist = tag.getFirst(FieldKey.ARTIST);

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
                            .artist(artist)
                            .build();

            String songId = persistSong(songMetadata);

            Artwork artwork = tag.getFirstArtwork();
            if (artwork != null) {
                byte[] imageData = artwork.getBinaryData();
                File thumbnail = new File(songId + ".jpg");
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                ImageIO.write(bufferedImage, "jpg", thumbnail);

                // save thumbnail file
            }

            List<StoredUser> userList = userDao.findAll();
            userList.forEach(
                    user -> {
                        // todo: predict song by model id (userId);
                        persistUserSongMapping(user.getId(), songId, Emotion.HAPPY);
                    }
            );

            // save mp3 file

        } catch (Exception e) {
            log.info("Error while getting audio details! ", e);
            return ResponseEntity.internalServerError().body("Song processing failed!");
        }

        return ResponseEntity.ok().body("Song processing triggered successfully!");
    }

    @Override
    public String registerUser(UserDto userDto) {
        if (Objects.isNull(userDao.findByEmailId(userDto.getEmailId()))) {
            userDao.save(UserMapper.toEntity(userDto));
            // todo: add model space and rename model as the userId
            userSongMappingDao.addSongsForUser(userDto.getUserId());
            return "User added successfully!";
        }

        return "User Already Registered!";
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

    private void persistUserSongMapping(
            String userId, String songId, Emotion emotion) {
        userSongMappingDao.addSong(userId, songId, emotion);
    }
}
