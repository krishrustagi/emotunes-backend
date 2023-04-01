package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.dto.UserDto;
import com.emotunes.emotunes.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/admin")
@Api("Admin Controller")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping(value = "/song/add", consumes = "multipart/form-data")
    @ApiOperation(value = "Upload song file")
    public ResponseEntity<String> addSong(
            @RequestPart("file") MultipartFile songFile)
            throws IOException {

        return adminService.addSong(songFile);
    }

    @PostMapping(value = "user/register")
    @ApiOperation("Register user")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(adminService.registerUser(userDto));
    }

}
