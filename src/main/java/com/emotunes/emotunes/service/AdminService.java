package com.emotunes.emotunes.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {

    ResponseEntity<String> addSong(MultipartFile songFile) throws IOException;
}
