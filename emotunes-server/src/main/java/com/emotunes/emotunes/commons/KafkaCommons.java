package com.emotunes.emotunes.commons;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaCommons {

    public static final String PROJECT_PREFIX = "emotunes_";

    public static final String GROUP_ID_SUFFIX = "_consumer-group";

    public static final String SONG_METADATA_EVENT_TOPIC = "song-metadata-event-topic";
}
