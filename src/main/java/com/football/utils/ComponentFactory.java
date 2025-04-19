package com.football.utils;

import com.football.components.API;
import lombok.experimental.UtilityClass;

import static com.football.listeners.APIListener.getComponentCache;
import static org.joor.Reflect.onClass;

/**
 * A factory class to create new components in a generic way.
 */
@UtilityClass
@SuppressWarnings("HideUtilityClassConstructor")
public class ComponentFactory {

    @SuppressWarnings("unchecked")
    public static <T extends API, V> T use(final Class<T> componentClass, final V... values) {
        return (T) getComponentCache()
                .map(cache -> cache.get(componentClass, page -> onClass(page).create(values).get()))
                .orElseGet(() -> onClass(componentClass).create(values).get());
    }

    @SafeVarargs
    public static <T extends API, V> T at(final Class<T> componentClass, final V... values) {
        return use(componentClass, values);
    }

    public static <T> T instanceOf(final Class<T> componentClass) {
        return onClass(componentClass).create().get();
    }
}
