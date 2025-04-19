package com.football.dataproviders;

import com.football.components.PlayerAPI;
import com.football.model.PlayerRequestDTO;
import com.football.model.PlayerResponseDTO;
import com.football.model.combined.PlayerData;
import com.football.provider.ControllerRole;
import com.football.provider.PlayerRole;

import static com.football.config.APIConfig.CONFIG;
import static com.football.provider.ControllerRole.SUPERVISOR;
import static com.football.utils.ComponentFactory.instanceOf;
import static com.football.utils.ComponentFactory.use;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Player data provider interface.
 */
@SuppressWarnings("FinalLocalVariable")
public interface IPlayerDataProvider {

    default PlayerData playerData() {
        return playerData(SUPERVISOR, PlayerRole.ADMIN);
    }

    default PlayerData playerData(final ControllerRole role, final PlayerRole playerRole) {
        var expectedPlayer = instanceOf(PlayerRequestDTO.class)
                .generatePlayer()
                .withLogin(playerRole.value() + randomAlphanumeric(CONFIG.randomCharactersAmount()))
                .withRole(playerRole);

        var actualPlayer = use(PlayerAPI.class)
                .createPlayer(expectedPlayer, role)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .as(PlayerResponseDTO.class);

        return instanceOf(PlayerData.class)
                .withActualPlayer(actualPlayer)
                .withExpectedPlayer(expectedPlayer);
    }
}

