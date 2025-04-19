package com.football.provider;

import lombok.Getter;

/**
 * Lists all available player roles in Application.
 */
@Getter
public enum PlayerRole implements IValuable {
    ADMIN,
    USER;

    @Override
    public String value() {
        return name().toLowerCase();
    }
}
