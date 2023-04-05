package com.emotunes.emotunes.service.impl;

import com.emotunes.emotunes.enums.Emotion;
import com.emotunes.emotunes.service.UserSongModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserSongModelServiceImpl implements UserSongModelService {

    @Override
    public Emotion predictEmotion(String trainingModelId, String songUrl) throws IOException {
        String predictEmotionFileName = "predictSongEmotion.py";
        String scriptPath = new ClassPathResource(predictEmotionFileName).getFile().getAbsolutePath();

        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(scriptPath);
        command.add(trainingModelId);
        command.add(songUrl);

        String response = runProcessBuilder(command);

        return Emotion.valueOf(response.trim());
    }

    private String runProcessBuilder(List<String> command) throws IOException {

        StringBuilder response = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line).append("\n");
        }

        return response.toString();
    }
}