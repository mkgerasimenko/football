package com.football.model.combined;

import com.football.model.PlayerRequestDTO;
import com.football.model.PlayerResponseDTO;
import lombok.Data;

/**
 * Player data storage.
 */
@Data
public class PlayerData {
    private PlayerRequestDTO expectedPlayer;
    private PlayerResponseDTO actualPlayer;

    public PlayerData withActualPlayer(final PlayerResponseDTO actualPlayer) {
        this.actualPlayer = actualPlayer;
        return this;
    }

    public PlayerData withExpectedPlayer(final PlayerRequestDTO expectedPlayer) {
        this.expectedPlayer = expectedPlayer;
        return this;
    }
}
