package com.football.components;

import com.football.model.PlayerRequestDTO;
import com.football.model.PlayerResponseDTO;
import com.football.provider.ControllerRole;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Player DSL for basic CRUD operations.
 */
public class PlayerAPI extends API {

    @Step("Create \"{player.screenName}\" player with role \"{player.role}\" within editor \"{editor}\"")
    public Response createPlayer(final PlayerRequestDTO player, final String editor) {
        return given()
                .params(player.convert())
                .get(rootPath() + "create/" + editor);
    }

    public Response createPlayer(final PlayerRequestDTO player, final ControllerRole role) {
        return createPlayer(player, role.value());
    }

    public Response createPlayer(final PlayerRequestDTO player) {
        return createPlayer(player, ControllerRole.SUPERVISOR);
    }

    @Step("Update \"{player.login}\" player within editor \"{editor}\" and \"{id}\" id")
    public Response updatePlayer(final PlayerRequestDTO player, final ControllerRole editor, final Long id) {
        return given()
                .contentType(ContentType.JSON)
                .body(player)
                .patch(rootPath() + "update/" + editor.value() + "/" + id);
    }

    @Step("Read player #{player.id}")
    public Response readPlayer(final PlayerResponseDTO player) {
        return given()
                .contentType(ContentType.JSON)
                .body(player.createRequestBody())
                .when()
                .post(rootPath() + "get");
    }

    @Step("Read all players")
    public Response readPlayers() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(rootPath() + "get/all");
    }

    @Step("Update \"{player.screenName}\" player within editor \"{editor}\" and \"{id}\"")
    public Response deletePlayer(final PlayerResponseDTO player, final ControllerRole editor) {
        return given()
                .contentType(ContentType.JSON)
                .body(player.createRequestBody())
                .delete(rootPath() + "delete/" + editor.value());
    }

    public Response deletePlayer(final PlayerResponseDTO player) {
        return deletePlayer(player, ControllerRole.SUPERVISOR);
    }

    @Override
    public String rootPath() {
        return "/player/";
    }
}
