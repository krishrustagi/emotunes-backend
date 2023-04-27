package com.emotunes.emotunes.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AzureStorageConstans {

    public static final String SONGS_CONTAINER = "songs";

    public static final String THUMBNAILS_CONTAINER = "thumbnails";

    public static final String DEFAULT_THUMBNAIL_URL = "https://emotunes.blob.core.windows" +
            ".net/thumbnails/default-thumbnail.jpg";
}
