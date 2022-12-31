package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.dto.SongDto;
import com.emotunes.emotunes.service.AdminService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/admin/")
@Api("Admin Controller")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/song/add")
    public ResponseEntity addSong(@RequestBody SongDto songDto) {
        adminService.addSong(songDto);
        return ResponseEntity.ok().build();
    }
}
