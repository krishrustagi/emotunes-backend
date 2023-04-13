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

        Emotion emotion;
        try {
            String response = runProcessBuilder(command);
            emotion = Emotion.valueOf(response.trim());
        } catch (Exception e) { // todo: proper handling for such cases
            log.error("Error while fetching emotion!", e);
            emotion = Emotion.NEUTRAL;
        }

        return emotion;
    }

    private String runProcessBuilder(List<String> command) throws IOException {

        StringBuilder response = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error("Error while reading python script!", e);
            throw e;
        } finally {
            process.destroy();
        }

        return response.toString();
    }
}