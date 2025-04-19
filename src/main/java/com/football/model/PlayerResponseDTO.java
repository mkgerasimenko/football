package com.football.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import one.util.streamex.StreamEx;

import java.util.Map;

import static org.apache.commons.lang3.RandomUtils.nextLong;

/**
 * PlayerResponseDTO payload.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerResponseDTO extends Player {
    public Long id;

    public Map<String, Object> createRequestBody() {
        return StreamEx.of("playerId")
                .toMap(key -> key, key -> this.id);
    }

    public PlayerResponseDTO withRandomId() {
        this.id = nextLong();
        return this;
    }
}
