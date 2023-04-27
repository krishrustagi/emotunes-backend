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

@RestController
@RequestMapping("/v1/song_player")
@Api("Song Player Controller")
@RequiredArgsConstructor
public class SongPlayerController {

    private final SongPlayerService songPlayerService;

    @PostMapping("/toggle_like")
    @ApiOperation(("Toggle like"))
    public ResponseEntity<String> toggleLike(
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "song_id") String songId) {
        return ResponseEntity.ok(songPlayerService.toggleLike(userId, songId));
    }

    @PostMapping("/user_song_emotion_preference")
    @ApiOperation("user song emotion preferences")
    public ResponseEntity<String> userSongEmotionPreference(
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "song_id") String songId,
            @RequestParam("correct_emotion") Emotion correctEmotion) {
        return ResponseEntity.ok(songPlayerService.userSongEmotionPreference(
                userId, songId, correctEmotion));
    }
}
