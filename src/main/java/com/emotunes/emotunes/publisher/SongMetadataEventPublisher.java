package com.emotunes.emotunes.publisher;

import com.emotunes.emotunes.dto.SongMetadata;

public interface SongMetadataEventPublisher {

    void publish(SongMetadata songMetadata);
}
