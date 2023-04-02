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
import com.emotunes.emotunes.util.IdGenerationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
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

    private static final int BULK_SONGS_LIMIT = 50;

    private final SongsDao songsDao;
    private final UserDao userDao;
    private final UserSongMappingDao userSongMappingDao;

    @Override
    public ResponseEntity<String> addSongs(List<MultipartFile> songFiles) { // todo: use multithreading
        if (songFiles.size() > BULK_SONGS_LIMIT) {
            return ResponseEntity.internalServerError().body("Maximum 50 files at a time allowed!");
        } else {
            songFiles.forEach(songFile -> {
                try {
                    addSong(songFile);
                } catch (IOException | CannotReadException | TagException | InvalidAudioFrameException |
                         ReadOnlyFileException e) {
                    log.error("Error while adding song {}", songFile, e);
                    throw new RuntimeException();
                }
            });
        }

        return ResponseEntity.ok("All songs uploaded successfully!");
    }

    private void addSong(MultipartFile songFile)
            throws IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException {

        File file = convertToAudioFile(songFile);

        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            String title = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);

            long duration = getDuration(audioFile);

            String thumbnailUrl = saveThumbnail(tag);

            SongMetadata songMetadata =
                    SongMetadata.builder()
                            .title(title)
                            .duration(Instant.ofEpochSecond(duration).atZone(
                                    ZoneId.of("UTC")
                            ).toLocalTime().toString())
                            .artist(artist)
                            .thumbnailUrl(thumbnailUrl)
                            .songUrl("") // todo: update url
                            .build();

            // todo: save mp3 song file

            String songId = persistSong(songMetadata);

            availSongToAllUsers(songId);

        } catch (Exception e) {
            log.error("Error while getting audio details! ", e);
            throw e;
        }
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

    private File convertToAudioFile(MultipartFile file) throws IOException {
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

    private void persistUserSongMapping(String userId, String songId, Emotion emotion) {
        userSongMappingDao.addSong(userId, songId, emotion);
    }

    private long getDuration(AudioFile audioFile) {
        return audioFile.getAudioHeader().getTrackLength();
    }

    private String saveThumbnail(Tag tag) throws IOException {
        Artwork artwork = tag.getFirstArtwork();
        if (artwork != null) {
            byte[] imageData = artwork.getBinaryData();
            String thumbnailFileName = IdGenerationUtil.getRandomId();
            File thumbnail = new File(thumbnailFileName + ".jpg");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageIO.write(bufferedImage, "jpg", thumbnail);

            // save thumbnail file with thumbnailFileName
        }

        return ""; // todo: return thumbnail url
    }

    private void availSongToAllUsers(String songId) {
        List<StoredUser> userList = userDao.findAll();
        userList.forEach(
                user -> {
                    // todo: predict song by model id (userId);
                    persistUserSongMapping(user.getId(), songId, Emotion.HAPPY);
                }
        );
    }
}
