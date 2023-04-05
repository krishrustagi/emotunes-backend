package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/songs/")
@Api("Song Controller")
@RequiredArgsConstructor
public class SongsController {

    private final SongService songService;

    @GetMapping("/all")
    @ApiOperation("Get All Songs") // todo: add limit
    public ResponseEntity<List<SongMetadata>> getAllSongs(
            @RequestParam(value = "user_id") String userId) {
        return ResponseEntity.ok(songService.getAllSongs(userId));
    }

    @GetMapping("/search")
    @ApiOperation("Search songs by Prefix")
    public ResponseEntity<List<SongMetadata>> getSongsByPrefix( // todo: edit distance
                                                                @RequestParam("user_id") String userId,
                                                                @RequestParam("prefix") String prefix) {
        return ResponseEntity.ok(songService.getSongsByPrefix(userId, prefix));
    }

    @GetMapping("/emotion")
    @ApiOperation("Get Songs By Emotion") // todo: add paging
    public ResponseEntity<List<SongMetadata>> getSongsByEmotion(
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "emotion") Emotion emotion) {
        return ResponseEntity.ok(songService.getSongsByEmotion(userId, emotion));
    }

    @GetMapping("liked")
    @ApiOperation("Get All liked songs")
    public ResponseEntity<List<SongMetadata>> getLikedSongs( // todo: add paging
                                                             @RequestParam("user_id") String userId) {
        return ResponseEntity.ok(songService.getLikedSongs(userId));
    }

    // todo: show liked songs based on emotions
}
