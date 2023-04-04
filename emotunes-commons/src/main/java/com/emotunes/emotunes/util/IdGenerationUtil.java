package com.emotunes.emotunes.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IdGenerationUtil {

    public static String getRandomId() {
        return NanoIdUtils.randomNanoId();
    }
}
