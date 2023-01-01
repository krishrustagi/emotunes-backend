package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/v1/songs/")
@Api("Song Controller")
@RequiredArgsConstructor
public class SongsController {

    private final SongService songService;

    @GetMapping("/all")
    @ApiOperation("Get All Songs")
    public ResponseEntity<List<SongMetadata>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping("/search")
    @ApiOperation("Search songs by Prefix")
    public ResponseEntity<List<SongMetadata>> getSongsByPrefix(
            @RequestParam("prefix") String prefix) {
        return ResponseEntity.ok(songService.getSongsByPrefix(prefix));
    }

    @GetMapping("/emotion")
    @ApiOperation("Get Songs By Emotion")
    public ResponseEntity<List<SongMetadata>> getSongsByEmotion(
            @RequestParam(value = "emotion", required = false) Emotion emotion) {
        return ResponseEntity.ok(songService.getSongsByEmotion(emotion));
    }

    @GetMapping("liked")
    @ApiOperation("Get All liked songs")
    public ResponseEntity<List<SongMetadata>> getLikedSongs(
            @RequestParam("user_id") String userId) {
        return ResponseEntity.ok(songService.getLikedSongs(userId));
    }
}
