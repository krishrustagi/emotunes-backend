package com.emotunes.emotunes.service;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {

    ResponseEntity<String> addSong(MultipartFile songFile) throws IOException, CannotReadException,
            TagException, InvalidAudioFrameException, ReadOnlyFileException;
}
