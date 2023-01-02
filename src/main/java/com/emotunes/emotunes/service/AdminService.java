package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.SongMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {

    void persistSong(SongMetadata songMetadata);

    ResponseEntity<String> addSong(MultipartFile songFile) throws IOException;
}
