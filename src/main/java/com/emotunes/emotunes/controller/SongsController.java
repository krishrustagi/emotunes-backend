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

    private static final int NUMBER_OF_SONGS_TO_FETCH = 10;

    @GetMapping("/all")
    @ApiOperation("Get All Songs")
    public ResponseEntity<List<SongMetadata>> getAllSongs(
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "last_fetched_id") String lastFetchedId) {
        return ResponseEntity.ok(songService.getNextPageOfSongsFromAllCategories(userId, lastFetchedId, NUMBER_OF_SONGS_TO_FETCH));
    }

    @GetMapping("/search")
    @ApiOperation("Search songs by Prefix")// todo: edit distance
    public ResponseEntity<List<SongMetadata>> getSongsByPrefix(
            @RequestParam("user_id") String userId, @RequestParam("prefix") String prefix) {
        return ResponseEntity.ok(songService.getSongsByPrefix(userId, prefix));
    }

    @GetMapping("/emotion")
    @ApiOperation("Get Songs By Emotion") // todo: add paging
    public ResponseEntity<List<SongMetadata>> getSongsByEmotion(
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "last_fetched_id") String lastFetchedId,
            @RequestParam(value = "emotion") Emotion emotion) {
        return ResponseEntity.ok(songService.getNextPageOfSongsByEmotion(userId, lastFetchedId, emotion,
                NUMBER_OF_SONGS_TO_FETCH));
    }

    @GetMapping("liked")
    @ApiOperation("Get All liked songs")
    public ResponseEntity<List<SongMetadata>> getLikedSongs(
            @RequestParam("user_id") String userId,
            @RequestParam("last_fetched_id") String lastFetchedId) {
        return ResponseEntity.ok(songService.getNextPageOfLikedSongs(userId, lastFetchedId, NUMBER_OF_SONGS_TO_FETCH));
    }

    // todo: show liked songs based on emotions
}
