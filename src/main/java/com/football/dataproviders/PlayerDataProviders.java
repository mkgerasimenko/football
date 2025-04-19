package com.football.dataproviders;

import com.football.components.PlayerAPI;
import com.football.model.PlayerRequestDTO;
import com.football.model.PlayerResponseDTO;
import com.football.model.combined.PlayerData;
import com.football.provider.PlayerRole;
import io.github.sskorol.core.DataSupplier;
import io.vavr.Tuple;
import one.util.streamex.StreamEx;

import static com.football.utils.ComponentFactory.at;
import static com.football.utils.ComponentFactory.instanceOf;

/**
 * Player data providers.
 */
public class PlayerDataProviders implements IPlayerDataProvider {

    @DataSupplier(flatMap = true)
    public StreamEx<PlayerRequestDTO> createSupervisorPlayerData() {
        return StreamEx.of(PlayerRole.values())
                .map(role -> instanceOf(PlayerRequestDTO.class).generatePlayer().withRole(role));
    }

    @DataSupplier(flatMap = true)
    public StreamEx<Tuple> createAdminPlayerData() {
        var playerData = playerData();
        return StreamEx.of(PlayerRole.values())
                .map(role -> Tuple.of(instanceOf(PlayerRequestDTO.class)
                        .generatePlayer()
                        .withRole(role), playerData.getActualPlayer().getLogin()));
    }

    @DataSupplier(transpose = true)
    public PlayerData updatePlayerData() {
        var playerData = playerData();

        var playerResponseDTO = at(PlayerAPI.class)
                .readPlayer(playerData.getActualPlayer())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PlayerResponseDTO.class);

        return playerData
                .withActualPlayer(playerResponseDTO)
                .withExpectedPlayer(playerData.getExpectedPlayer().withRandomSecretName());
    }

    @DataSupplier
    public PlayerData readPlayerData() {
        return playerData();
    }

    @DataSupplier
    public PlayerResponseDTO readDamagedPlayerData() {
        return instanceOf(PlayerResponseDTO.class).withRandomId();
    }

    @DataSupplier
    public PlayerRequestDTO wrongPlayerRequestData() {
        return instanceOf(PlayerRequestDTO.class).generatePlayer();
    }

    @DataSupplier
    public PlayerResponseDTO wrongPlayerResponseData() {
        return instanceOf(PlayerResponseDTO.class);
    }
}
