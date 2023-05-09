package com.emotunes.emotunes.helper;

import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dao.UserDao;
import com.emotunes.emotunes.dao.UserSongMappingDao;
import com.emotunes.emotunes.entity.StoredSong;
import com.emotunes.emotunes.entity.StoredUser;
import com.emotunes.emotunes.enums.Emotion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.emotunes.emotunes.constants.AzureStorageConstans.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminHelper {

    private final FileUploadHelper fileUploadHelper;
    private final UserDao userDao;
    private final SongsDao songsDao;
    private final UserSongMappingDao userSongMappingDao;
    private final MachineLearningHelper machineLearningHelper;

    public String uploadSongFileAndGetUrl(MultipartFile file) throws IOException {
        return fileUploadHelper.uploadAndGetUrl(SONGS_CONTAINER, file.getInputStream(),
                file.getOriginalFilename(), file.getSize());
    }

    public String uploadThumbnailAndGetUrl(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return fileUploadHelper.uploadAndGetUrl(THUMBNAILS_CONTAINER, inputStream,
                    file.getName(),
                    file.length());
        } catch (Exception e) {
            return DEFAULT_THUMBNAIL_URL;
        }
    }

    public void availSongToAllUsers(String songId, String songUrl) {
        List<StoredUser> userList = userDao.findAll();
        for (StoredUser user : userList) {
            String songEmotion = machineLearningHelper.predictSongEmotion(songUrl, user.getModelWeightsUrl());
            persistUserSongMapping(user.getId(), songId, Emotion.valueOf(songEmotion));
        }
    }

    public void availAllSongsToNewUser(String userId) {
        List<StoredSong> songList = songsDao.getAllSongs();
        songList.forEach(song -> {
            userSongMappingDao.addMapping(userId, song.getId(), song.getDefaultEmotion());
        });
    }

    // --- Private Methods ---- //

    private void persistUserSongMapping(String userId, String songId, Emotion emotion) {
        userSongMappingDao.addMapping(userId, songId, emotion);
    }

}
