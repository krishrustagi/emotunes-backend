package com.emotunes.emotunes.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.emotunes.emotunes.commons.KafkaCommons.PROJECT_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHelper {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, String key, Object msg) {
        log.info("Publishing to kafka topic: {}", topic);
        kafkaTemplate.send(topic, key, msg);
    }

    public void publish(String topic, Object msg) {
        log.info("Publishing to kafka topic: {}", topic);
        kafkaTemplate.send(PROJECT_PREFIX + topic, msg);
    }
}
