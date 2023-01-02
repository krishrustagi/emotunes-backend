package com.emotunes.emotunes.subscriber;

import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SongMetadataEventSubscriber {

    private final AdminService adminService;

    @KafkaListener(
            topics = "ABC",
            groupId = "abc",
            containerFactory = "kafkaListenerContainerFactory",
            concurrency = "1")
    public void receive(SongMetadata songMetadata, Acknowledgment ack) {
        log.info("Message Received: {}", songMetadata);
        try {
            adminService.persistSong(songMetadata);
            ack.acknowledge();
        } catch (Exception e) {
            log.info("Error while saving the song metadata: {}", songMetadata);
            throw e;
        }
    }
}
