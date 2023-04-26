package com.emotunes.emotunes.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import static com.emotunes.emotunes.constants.AzureStorageConstans.SONGS_CONTAINER;
import static com.emotunes.emotunes.constants.AzureStorageConstans.THUMBNAILS_CONTAINER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

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
            String songUrl = uploadSongFileAndGetUrl(songFile);
            AudioFile audioFile = AudioFileIO.read(convertToAudioFile(songFile));
            Tag tag = audioFile.getTag();
            String title = getTitle(tag);
            String artist = getArtistName(tag.getFirst(FieldKey.ARTIST));

            long duration = getDuration(audioFile);

            String thumbnailUrl = saveThumbnail(tag, title);
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

            String songId = persistSong(songMetadata);

            availSongToAllUsers(songId, songUrl);

        } catch (Exception e) {
            log.error("Error while getting audio details! ", e);
            throw e;
        }
    }

    private String persistSong(SongMetadata songMetadata) {
        return songsDao.addSong(songMetadata);
    }

    private void persistUserSongMapping(String userId, String songId, Emotion emotion) {
        userSongMappingDao.addMapping(userId, songId, emotion);
    }

    private long getDuration(AudioFile audioFile) {
        return audioFile.getAudioHeader().getTrackLength();
    }

    private String saveThumbnail(Tag tag, String title) throws IOException {
        Artwork artwork = tag.getFirstArtwork();
        String thumbnailFileName = IdGenerationUtil.getRandomId();
        File thumbnail = new File(thumbnailFileName + ".jpg");
        try {
            byte[] imageData = artwork.getBinaryData();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageIO.write(bufferedImage, "jpg", thumbnail);

            String thumbnailUrl = uploadThumbnailAndGetUrl(thumbnail);
            log.info("thumbnail Url: {}", thumbnailUrl);
            return thumbnailUrl;
        } catch (Exception e) {
            log.error("Error while fetching thumbnail");
        } finally {
            Files.delete(Path.of(thumbnailFileName + ".jpg"));
        }

        return ""; // todo: return default thumbnail url
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

    private String uploadAndGetUrl(String containerName, InputStream inputStream, String fileName, long fileSize) {
        BlobServiceClient blobServiceClient =
                new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(inputStream, fileSize);

        return blobClient.getBlobUrl();
    }

    private String uploadSongFileAndGetUrl(MultipartFile file) throws IOException {
        return uploadAndGetUrl(SONGS_CONTAINER, file.getInputStream(), file.getOriginalFilename(), file.getSize());
    }

    private String uploadThumbnailAndGetUrl(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return uploadAndGetUrl(THUMBNAILS_CONTAINER, inputStream, file.getName(), file.length());
        } catch (Exception e) {
            return ""; // todo: return default thumbnail url
        }
    }
}
