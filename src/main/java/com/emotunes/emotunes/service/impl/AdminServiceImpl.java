package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.config.KafkaHelper;
import com.emotunes.emotunes.dao.SongsDao;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final SongsDao songsDao;
    private final KafkaHelper kafkaHelper;

    @Override
    public void persistSong(SongMetadata songMetadata) {
        // todo: getEmotion by predicting the song using Emotion.HAPPY for now
        songsDao.addSong(songMetadata, Emotion.HAPPY);
    }

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

            kafkaHelper.publish("ABC", songMetadata);
        } catch (Exception e) {
            log.info("Error while getting audio details! ", e);
            return ResponseEntity.internalServerError().body("Song processing failed!");
        } finally {
            Files.delete(file.toPath());
        }

        return ResponseEntity.ok().body("Song processing triggered successfully!");
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }

        return convFile;
    }
}
