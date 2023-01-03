package com.emotunes.emotunes.publisher.event;

import com.emotunes.emotunes.config.KafkaHelper;
import com.emotunes.emotunes.dto.SongMetadata;
import com.emotunes.emotunes.publisher.SongMetadataEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.emotunes.emotunes.commons.KafkaCommons.PROJECT_PREFIX;
import static com.emotunes.emotunes.commons.KafkaCommons.SONG_METADATA_EVENT_TOPIC;

@Component
@RequiredArgsConstructor
public class SongMetadataEventKafkaPublisher
        implements SongMetadataEventPublisher {

    private final KafkaHelper kafkaHelper;

    @Override
    public void publish(SongMetadata songMetadata) {
        kafkaHelper.publish(SONG_METADATA_EVENT_TOPIC, songMetadata);
    }
}
