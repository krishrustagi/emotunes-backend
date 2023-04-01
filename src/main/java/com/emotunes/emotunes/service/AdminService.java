package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {

    ResponseEntity<String> addSong(MultipartFile songFile) throws IOException;

    String registerUser(UserDto userDto);
}
