package com.football.apitests;

import com.football.components.PlayerAPI;
import com.football.dataproviders.PlayerDataProviders;
import com.football.model.PlayerRequestDTO;
import com.football.model.PlayerResponseDTO;
import com.football.model.combined.PlayerData;
import com.football.provider.ControllerRole;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.function.Function;

import static com.football.provider.ControllerRole.SUPERVISOR;
import static com.football.utils.ComponentFactory.at;
import static com.football.utils.ComponentFactory.use;
import static com.football.utils.RandomUtils.randomLong;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class ApiPlayerTests {

    @Test(dataProvider = "createSupervisorPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldCreatePlayerViaSupervisor(PlayerRequestDTO expectedPlayer) {
        verifyPlayer(processPlayerAndValidate(role ->
                use(PlayerAPI.class)
                        .createPlayer(expectedPlayer)), expectedPlayer);
    }

    @Test(dataProvider = "createSupervisorPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldNotCreatePlayerViaSupervisor(PlayerRequestDTO expectedPlayer) {
        validatePlayerCreation(role -> use(PlayerAPI.class)
                .createPlayer(expectedPlayer.withRole(role)));
    }

    @Test(dataProvider = "createAdminPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldCreatePlayerViaAdmin(PlayerRequestDTO expectedPlayer, String editor) {
        verifyPlayer(processPlayerAndValidate(role ->
                use(PlayerAPI.class)
                        .createPlayer(expectedPlayer, editor)), expectedPlayer);
    }

    @Test(dataProvider = "createAdminPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldNotCreatePlayerViaAdmin(PlayerRequestDTO expectedPlayer, String editor) {
        validatePlayerCreation(role -> use(PlayerAPI.class)
                .createPlayer(expectedPlayer.withRole(role), editor));
    }

    @Test(dataProvider = "updatePlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldUpdatePlayerViaSupervisor(PlayerData data) {
        verifyPlayer(processPlayerAndValidate(role ->
                use(PlayerAPI.class)
                        .updatePlayer(data.getExpectedPlayer(),
                                SUPERVISOR,
                                data.getActualPlayer().getId())), data.getExpectedPlayer());
    }

    @Test(dataProvider = "wrongPlayerRequestData", dataProviderClass = PlayerDataProviders.class)
    public void shouldNotUpdatePlayerViaSupervisor(PlayerRequestDTO player) {
        use(PlayerAPI.class)
                .updatePlayer(player, SUPERVISOR, randomLong())
                .then()
                .assertThat()
                .statusCode(200)
                .body(emptyString());
    }

    @Test(dataProvider = "readPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldReadPlayer(PlayerData data) {
        verifyPlayer(data.getActualPlayer(), data.getExpectedPlayer());
    }

    @Test(dataProvider = "readDamagedPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldNotReadPlayer(PlayerResponseDTO playerResponseDTO) {
        at(PlayerAPI.class)
                .readPlayer(playerResponseDTO)
                .then()
                .assertThat()
                .statusCode(200)
                .body(emptyString());
    }

    @Test(dataProvider = "createSupervisorPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldDeletePlayerViaSupervisor(PlayerRequestDTO expectedPlayer) {
        processAndDeletePlayer(role -> use(PlayerAPI.class).createPlayer(expectedPlayer));
    }

    @Test(dataProvider = "wrongPlayerResponseData", dataProviderClass = PlayerDataProviders.class)
    public void shouldNotDeletePlayerViaSupervisor(PlayerResponseDTO playerResponseDTO) {
        at(PlayerAPI.class)
                .deletePlayer(playerResponseDTO.withRandomId())
                .then()
                .assertThat()
                .statusCode(403)
                .body(emptyString());
    }

    @Test(dataProvider = "createAdminPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldDeletePlayerViaAdmin(PlayerRequestDTO expectedPlayer, String editor) {
        processAndDeletePlayer(role -> use(PlayerAPI.class)
                .createPlayer(expectedPlayer, editor));
    }

    @Test(dataProvider = "createSupervisorPlayerData", dataProviderClass = PlayerDataProviders.class)
    public void shouldSearchPlayer(PlayerRequestDTO expectedPlayer) {
        var actualPlayer = processPlayerAndValidate(role ->
                use(PlayerAPI.class)
                        .createPlayer(expectedPlayer));

        assertThat(players())
                .extracting(PlayerResponseDTO::getScreenName)
                .containsOnlyOnce(readCreatedPlayer(actualPlayer).getScreenName());
    }

    @Test
    public void shouldNotSearchPlayer() {
        assertThat(players())
                .extracting(PlayerResponseDTO::getScreenName)
                .doesNotContain(randomAlphanumeric(6));
    }

    private void verifyPlayer(final PlayerResponseDTO createdPlayer, final PlayerRequestDTO expectedPlayer) {
        assertThat(readCreatedPlayer(createdPlayer))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedPlayer);
    }

    private void validatePlayerCreation(final Function<String, Response> playerCreator) {
        playerCreator.apply(randomAlphanumeric(6))
                .then()
                .assertThat()
                .statusCode(400)
                .body(emptyString());
    }

    private PlayerResponseDTO processPlayerAndValidate(final Function<String, Response> playerCreator) {
        return playerCreator.apply(randomAlphanumeric(6))
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .as(PlayerResponseDTO.class);
    }

    private void processAndDeletePlayer(final Function<String, Response> createPlayerFunction) {
        var playerResponseDTO = processPlayerAndValidate(createPlayerFunction);

        at(PlayerAPI.class)
                .deletePlayer(playerResponseDTO)
                .then()
                .assertThat()
                .statusCode(204)
                .body(emptyString());

        at(PlayerAPI.class)
                .readPlayer(playerResponseDTO)
                .then()
                .assertThat()
                .statusCode(200)
                .body(emptyString());
    }

    private PlayerResponseDTO readCreatedPlayer(PlayerResponseDTO actualPlayer) {
        return at(PlayerAPI.class)
                .readPlayer(actualPlayer)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PlayerResponseDTO.class);
    }

    private PlayerResponseDTO[] players() {
        return at(PlayerAPI.class)
                .readPlayers()
                .then()
                .assertThat()
                .statusCode(200)
                .body("players.size()", greaterThanOrEqualTo(ControllerRole.values().length))
                .extract()
                .jsonPath()
                .getObject("players", PlayerResponseDTO[].class);
    }
}
