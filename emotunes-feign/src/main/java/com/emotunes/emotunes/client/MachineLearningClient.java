package com.emotunes.emotunes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "machineLearningClient", url = "https://emtounes.onrender.com")
public interface MachineLearningClient {

    @PostMapping(value = "/v1/predict")
    String predictEmotion(@RequestBody Map<String, String> formData);

    @PostMapping(value = "/v1/re-train")
    ResponseEntity<byte[]> reTraining(@RequestBody Map<String, Object> formData);
}
