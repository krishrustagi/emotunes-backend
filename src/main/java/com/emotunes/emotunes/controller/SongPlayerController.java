package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("/v1/song_player")
@Api("Song Player Controller")
@RequiredArgsConstructor
public class SongPlayerController {

    private final SongPlayerService songPlayerService;

    @PostMapping("/user_song_response")
    @ApiOperation(("user Response to the song"))
    public ResponseEntity<String> userSongResponse(
            @RequestParam(value = "user_id", required = false) String userId,
            @RequestParam(value = "song_title", required = false) String songTitle,
            @RequestParam(value = "song_duration", required = false) String duration,
            @RequestParam("is_liked") boolean isLiked) {
        return songPlayerService.userSongResponse(
                userId, songTitle, LocalTime.parse(duration), isLiked);
    }

    @PostMapping("/song_not_per_emotion")
    @ApiOperation("Song not per emotion")
    public ResponseEntity<String> songNotPerEmotion(
            @RequestParam(value = "user_id", required = false) String userId,
            @RequestParam(value = "song_title", required = false) String songTitle,
            @RequestParam(value = "song_duration", required = false) String duration,
            @RequestParam("correct_emotion") Emotion correctEmotion) {
        songPlayerService.songNotPerEmotion(
                userId, songTitle, LocalTime.parse(duration), correctEmotion);
        return ResponseEntity.ok("Emotion fixed!");
    }
}
