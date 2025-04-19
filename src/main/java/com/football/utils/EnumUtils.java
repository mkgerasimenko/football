package com.football.utils;

import com.football.provider.IValuable;
import lombok.experimental.UtilityClass;
import one.util.streamex.StreamEx;

import static com.football.utils.RandomUtils.randomInt;

/**
 * Simplifies randomization stuff for Enum values.
 */
@SuppressWarnings({"HideUtilityClassConstructor", "FinalLocalVariable"})
@UtilityClass
public class EnumUtils {
    public static <T extends Enum<T> & IValuable> String getRandomValue(final Class<T> enumClass) {
        return StreamEx.of(enumClass.getEnumConstants())
                .map(IValuable::value)
                .toList()
                .get(randomInt(enumClass.getEnumConstants().length));
    }
}
