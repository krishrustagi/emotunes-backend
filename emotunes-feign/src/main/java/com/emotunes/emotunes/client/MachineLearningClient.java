package com.emotunes.emotunes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "machineLearningClient", url = "http://localhost:5000")
public interface MachineLearningClient {

    @PostMapping(value = "/v1/predict")
    String predictEmotion(@RequestBody LinkedMultiValueMap<String, String> formData);
}
