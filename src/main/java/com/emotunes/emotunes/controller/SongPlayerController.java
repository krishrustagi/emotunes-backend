package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.SongPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController("/v1/song_player")
@Api("Song Player Controller")
@RequiredArgsConstructor
public class SongPlayerController {

    private final SongPlayerService songPlayerService;

    @PostMapping("/like")
    @ApiOperation(("Like the song"))
    public ResponseEntity<String> likeCurrentSong(
            @RequestParam("user_id") String userId,
            @RequestParam("song_title") String songTitle,
            @RequestParam("song_duration") String duration) {
        songPlayerService.likeCurrentSong(userId, songTitle, LocalTime.parse(duration));
        return ResponseEntity.ok("Song Liked");
    }

    @PostMapping("/dislike")
    @ApiOperation(("Dislike the song"))
    public ResponseEntity<String> dislikeCurrentSong(
            @RequestParam("user_id") String userId,
            @RequestParam("song_id") String songId,
            @RequestParam("emotion") Emotion correctEmotion) {
        songPlayerService.dislikeCurrentSong(userId, songId, correctEmotion);
        return ResponseEntity.ok("Song DisLiked");
    }
}
