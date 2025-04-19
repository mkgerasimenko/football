package com.football.provider;

import lombok.Getter;

/**
 * Lists all available genders in Application.
 */
@Getter
public enum Gender implements IValuable {
    MALE,
    FEMALE;

    @Override
    public String value() {
        return name().toLowerCase();
    }
}
