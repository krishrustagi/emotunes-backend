package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/song_player")
@Api("Song Player Controller")
@RequiredArgsConstructor
public class SongPlayerController {

    private final SongPlayerService songPlayerService;

    @PostMapping("/play")
    @ApiOperation("Play")
    public void play(@RequestBody(required = false) SongMetadata songMetadata) {
        // todo: play song
    }

    @PostMapping("/is_liked")
    @ApiOperation(("Is liked?"))
    public ResponseEntity<String> userSongResponse(
            @RequestParam(value = "user_id") String userId,
            @RequestBody SongMetadata songMetadata,
            @RequestParam(value = "liked") boolean isLiked) {
        return songPlayerService.liked(userId, songMetadata, isLiked);
    }

    @PostMapping("/song_not_per_emotion")
    @ApiOperation("Song not per emotion")
    public ResponseEntity<String> songNotPerEmotion(
            @RequestParam(value = "user_id", required = false) String userId,
            @RequestBody(required = false) SongMetadata songMetadata,
            @RequestParam("correct_emotion") Emotion correctEmotion) {
        return songPlayerService.songNotPerEmotion(
                userId, songMetadata, correctEmotion);
    }
}
