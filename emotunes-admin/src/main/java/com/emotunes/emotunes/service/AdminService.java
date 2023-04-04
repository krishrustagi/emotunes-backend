package com.emotunes.emotunes.service;

import com.emotunes.emotunes.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    String addSongs(List<MultipartFile> songFiles) throws IOException;

    String registerUser(UserDto userDto);
}
