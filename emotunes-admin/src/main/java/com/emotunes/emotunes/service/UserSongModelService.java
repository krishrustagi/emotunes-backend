package com.emotunes.emotunes.service;

import com.emotunes.emotunes.enums.Emotion;

import java.io.IOException;

public interface UserSongModelService {

    Emotion predictEmotion(String trainingModelId, String songUrl) throws IOException;
}
