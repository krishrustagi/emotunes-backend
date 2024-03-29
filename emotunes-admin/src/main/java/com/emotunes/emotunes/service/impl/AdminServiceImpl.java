package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.helper.AdminHelper;
import com.emotunes.emotunes.helper.MachineLearningHelper;
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
import org.springframework.beans.factory.annotation.Value;
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

import static com.emotunes.emotunes.constants.AzureStorageConstans.DEFAULT_MODEL_WEIGHTS_URL;
import static com.emotunes.emotunes.constants.AzureStorageConstans.DEFAULT_THUMBNAIL_URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Value("${spring.profiles.active:prod}")
    private String env;

    private static final int BULK_SONGS_LIMIT = 50;

    private final SongsDao songsDao;
    private final UserDao userDao;
    private final AdminHelper adminHelper;
    private final MachineLearningHelper machineLearningHelper;

    @Override
    public String addSongs(List<MultipartFile> songFiles) {
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
            userDto.setModelWeightsUrl(DEFAULT_MODEL_WEIGHTS_URL);
            userDao.save(userDto);

            adminHelper.availAllSongsToNewUser(userDto.getUserId());
            return "User added successfully!";
        }

        return "User Already Registered!";
    }

    // --- Private Methods --- //

    private File convertToAudioFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()), ".mp3");
        file.transferTo(tempFile);
        return tempFile;
    }

    private void addSong(MultipartFile songFile)
            throws IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException,
            NullPointerException {

        try {
            String songUrl = null;
            if (!Objects.equals(env, "local"))
                songUrl = adminHelper.uploadSongFileAndGetUrl(songFile);

            AudioFile audioFile = AudioFileIO.read(convertToAudioFile(songFile));
            Tag tag = audioFile.getTag();
            String title = getTitle(tag);
            String artist = getArtistName(tag.getFirst(FieldKey.ARTIST));

            long duration = getDuration(audioFile);

            String thumbnailUrl = saveThumbnail(tag, title);

            Emotion defaultEmotion = Emotion.valueOf(
                    machineLearningHelper.predictSongEmotion(songUrl, DEFAULT_MODEL_WEIGHTS_URL));

            SongMetadata songMetadata =
                    SongMetadata.builder()
                            .title(title)
                            .duration(Instant.ofEpochSecond(duration).atZone(
                                    ZoneId.of("UTC")
                            ).toLocalTime().toString())
                            .artist(artist)
                            .thumbnailUrl(thumbnailUrl)
                            .songUrl(songUrl)
                            .emotion(defaultEmotion)
                            .build();

            String songId = persistSong(songMetadata);

            adminHelper.availSongToAllUsers(songId, songUrl, defaultEmotion);

        } catch (Exception e) {
            log.error("Error while getting audio details! ", e);
            throw e;
        }
    }

    private String persistSong(SongMetadata songMetadata) {
        return songsDao.addSong(songMetadata);
    }

    private long getDuration(AudioFile audioFile) {
        return audioFile.getAudioHeader().getTrackLength();
    }

    private String saveThumbnail(Tag tag, String title) throws IOException {
        Artwork artwork = tag.getFirstArtwork();
        String thumbnailFileName = IdGenerationUtil.getRandomId();
        File thumbnail = File.createTempFile(thumbnailFileName, ".jpg");
        try {
            byte[] imageData = artwork.getBinaryData();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageIO.write(bufferedImage, "jpg", thumbnail);

            String thumbnailUrl = null;
            if (!Objects.equals(env, "local"))
                thumbnailUrl = adminHelper.uploadThumbnailAndGetUrl(thumbnail);

            return thumbnailUrl;
        } catch (Exception e) {
            log.error("Error while fetching thumbnail!", e);
        }

        return DEFAULT_THUMBNAIL_URL;
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

            title = title.substring(0, title.indexOf("||"));
        } catch (NullPointerException e) {
            log.error("Title can't be null! ", e);
            throw e;
        }

        return title;
    }
}
