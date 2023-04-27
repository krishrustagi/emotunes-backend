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

    private static final int NUMBER_OF_SONGS_TO_BE_FETCHED = 50;
    private final SongService songService;

    @GetMapping("/all")
    @ApiOperation("Get Next Page Of Songs from All Category")
    public ResponseEntity<List<SongMetadata>> getAllSongs(
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "offset") Long offset) {
        return ResponseEntity.ok(songService.getAllSongs(userId, offset, NUMBER_OF_SONGS_TO_BE_FETCHED));
    }

    @GetMapping("/search")
    @ApiOperation("Search songs by Prefix")// todo: edit distance
    public ResponseEntity<List<SongMetadata>> getSongsByPrefix (
            @RequestParam("user_id") String userId, @RequestParam("prefix") String prefix,
            @RequestParam(value = "offset") Long offset){
        return ResponseEntity.ok(songService.getSongsByPrefix(userId, prefix, offset, NUMBER_OF_SONGS_TO_BE_FETCHED));
    }

    @GetMapping("/emotion")
    @ApiOperation("Get Next Page Of Songs By Emotion") // todo: add paging
    public ResponseEntity<List<SongMetadata>> getSongsByEmotion (
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "emotion") Emotion emotion,
            @RequestParam(value = "offset") Long offset){
        return ResponseEntity.ok(
                songService.getSongsByEmotion(userId, emotion, offset, NUMBER_OF_SONGS_TO_BE_FETCHED));
    }

    @GetMapping("liked")
    @ApiOperation("Get Next Page Of liked songs")
    public ResponseEntity<List<SongMetadata>> getLikedSongs (
            @RequestParam("user_id") String userId,
            @RequestParam(value = "offset") Long offset) {
        return ResponseEntity.ok(songService.getLikedSongs(userId, offset, NUMBER_OF_SONGS_TO_BE_FETCHED));
    }

        // todo: show liked songs based on emotions
}