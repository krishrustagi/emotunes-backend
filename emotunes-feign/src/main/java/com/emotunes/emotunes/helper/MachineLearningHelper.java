package com.emotunes.emotunes.helper;

import com.emotunes.emotunes.client.MachineLearningClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MachineLearningHelper {

    private final MachineLearningClient machineLearningClient;

    public String predictSongEmotion(String songUrl, String modelWeightsUrl) {
        Map<String, String> multiValueMap = new HashMap<>();
        multiValueMap.put("song_url", songUrl);
        multiValueMap.put("model_weights_url", modelWeightsUrl);

        return machineLearningClient.predictEmotion(multiValueMap);
    }

    public byte[] reTrainAndGetModelFile(String modelWeightsUrl, List<String> songUrls, List<String> emotions) {
        Map<String, Object> map = new HashMap<>();
        map.put("model_weights_url", modelWeightsUrl);
        map.put("song_urls", songUrls);
        map.put("emotions", emotions);

        return machineLearningClient.reTraining(map).getBody();
    }
}
