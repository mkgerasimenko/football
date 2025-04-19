package com.football.provider;

import lombok.Getter;

/**
 * Lists all available controller roles in Application.
 */
@Getter
public enum ControllerRole implements IValuable {
    ADMIN,
    SUPERVISOR;

    @Override
    public String value() {
        return name().toLowerCase();
    }
}
