package com.football.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Simplifies randomization stuff.
 */
@SuppressWarnings({"HideUtilityClassConstructor", "FinalLocalVariable"})
@UtilityClass
public class RandomUtils {
    public static Long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public static int randomInt(final int boundaryExclusive) {
        return ThreadLocalRandom.current().nextInt(boundaryExclusive);
    }

    public static int randomInt(final int fromInclusive, final int toExclusive) {
        return ThreadLocalRandom.current().nextInt(fromInclusive, toExclusive);
    }
}
