package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.AdminService;
import com.emotunes.emotunes.service.UserSongModelService;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
    private final UserSongModelService userSongModelService;

    @Override
    public String addSongs(List<MultipartFile> songFiles) { // todo: use multithreading
        if (songFiles.size() > BULK_SONGS_LIMIT) {
            throw new IllegalArgumentException("Maximum 50 files at a time allowed!");
        } else {
            songFiles.forEach(songFile -> {
                try {
                    addSong(songFile);
                } catch (IOException | CannotReadException | TagException | InvalidAudioFrameException |
                         ReadOnlyFileException | NullPointerException e) {
                    log.error("Error while adding song {}", songFile, e);
                }
            });
        }

        return "All songs uploaded successfully!";
    }

    @Override
    public String registerUser(UserDto userDto) {
        if (Objects.isNull(userDao.findByEmailId(userDto.getEmailId()))) {
            String trainingModelId = userDao.saveAndGetModelId(userDto);
            // todo: add model of user
            // todo: use kafka
            availAllSongsToUser(userDto.getUserId(), trainingModelId);
            return "User added successfully!";
        }

        return "User Already Registered!";
    }

    private File convertToAudioFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()), ".mp3");
        file.transferTo(tempFile);
        return tempFile;
    }

    private void addSong(MultipartFile songFile)
            throws IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException,
            NullPointerException {

        try {
            AudioFile audioFile = AudioFileIO.read(convertToAudioFile(songFile));
            Tag tag = audioFile.getTag();
            String title = getTitle(tag);
            String artist = getArtistName(tag.getFirst(FieldKey.ARTIST));

            long duration = getDuration(audioFile);

            String thumbnailUrl = saveThumbnail(tag);
            String songUrl = "";  // todo: update url
            SongMetadata songMetadata =
                    SongMetadata.builder()
                            .title(title)
                            .duration(Instant.ofEpochSecond(duration).atZone(
                                    ZoneId.of("UTC")
                            ).toLocalTime().toString())
                            .artist(artist)
                            .thumbnailUrl(thumbnailUrl)
                            .songUrl(songUrl)
                            .build();

            // todo: save mp3 song file

            String songId = persistSong(songMetadata);

            availSongToAllUsers(songId, songUrl);

        } catch (Exception e) {
            log.error("Error while getting audio details! ", e);
            throw e;
        }
    }

    private String persistSong(SongMetadata songMetadata) {
        // todo: save file with the song id;
        return songsDao.addSong(songMetadata);
    }

    private void persistUserSongMapping(String userId, String songId, Emotion emotion) {
        userSongMappingDao.addMapping(userId, songId, emotion);
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

    private void availSongToAllUsers(String songId, String songUrl) {
        List<StoredUser> userList = userDao.findAll();
        for (StoredUser user : userList) {
            predictEmotionAndPersistMapping(user.getId(), songId, songUrl, user.getTrainingModelId());
        }
    }

    private String getArtistName(String s) {
        if (s == null) {
            return "UNKNOWN";
        }
        return s;
    }

    private String getTitle(Tag tag) {
        String title;
        try {
            title = tag.getFirst(FieldKey.TITLE);

            title = title.replace('_', ' ');
            title = title.replaceAll("\\s*\\(\\s*", " (");
            title = title.replaceAll("\\s*\\)\\s*", ") ");
            if (title.endsWith(".mp3")) {
                title = title.substring(0, title.length() - 4);
            }
        } catch (NullPointerException e) {
            log.error("Title can't be null! ", e);
            throw e;
        }

        return title;
    }

    private void availAllSongsToUser(String userId, String trainingModelId) {
        List<StoredSong> songList = songsDao.getAllSongs();
        songList.forEach(song -> {
            predictEmotionAndPersistMapping(userId, song.getId(), song.getSongUrl(), trainingModelId);
        });
    }

    private void predictEmotionAndPersistMapping(
            String userId, String songId, String songUrl, String trainingModelId) {
        Emotion songEmotion = null;
        try {
            songEmotion = userSongModelService.predictEmotion(trainingModelId, songUrl);
        } catch (IOException e) {
            log.error("Error while adding user song mapping for user {} and song {}", userId, songId, e);
            songEmotion = Emotion.NEUTRAL;
        }

        persistUserSongMapping(userId, songId, songEmotion);
    }
}
