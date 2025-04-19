package com.football.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.provider.Gender;
import com.football.provider.PlayerRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

import static com.football.config.APIConfig.CONFIG;
import static com.football.utils.EnumUtils.getRandomValue;
import static com.football.utils.RandomUtils.randomInt;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

/**
 * PlayerRequestDTO payload.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlayerRequestDTO extends Player {
    public PlayerRequestDTO withRandomLogin() {
        this.login = randomAlphanumeric(CONFIG.randomCharactersAmount());
        return this;
    }

    public PlayerRequestDTO withLogin(final String login) {
        this.login = login;
        return this;
    }

    public PlayerRequestDTO withRandomPassword() {
        this.password = "Secret_" + randomAlphanumeric(CONFIG.randomCharactersAmount());
        return this;
    }

    public PlayerRequestDTO withPassword(final String password) {
        this.password = password;
        return this;
    }

    public PlayerRequestDTO withRandomSecretName() {
        this.screenName = "Screen_" + randomAlphanumeric(CONFIG.randomCharactersAmount());
        return this;
    }

    public PlayerRequestDTO withSecretName(final String secretName) {
        this.screenName = secretName;
        return this;
    }

    public PlayerRequestDTO withRandomGender() {
        this.gender = getRandomValue(Gender.class);
        return this;
    }

    public PlayerRequestDTO withRandomAge() {
        this.age = randomInt(CONFIG.minAge(), CONFIG.maxAge());
        return this;
    }

    public PlayerRequestDTO withAge(final Integer age) {
        this.age = age;
        return this;
    }

    public PlayerRequestDTO withRandomRole() {
        this.role = getRandomValue(PlayerRole.class);
        return this;
    }

    public PlayerRequestDTO withRole(final PlayerRole role) {
        return this.withRole(role.value());
    }

    public PlayerRequestDTO withRole(final String role) {
        this.role = role;
        return this;
    }

    public Map<String, Object> convert() {
        return new ObjectMapper()
                .convertValue(this, new TypeReference<>() {
                });
    }

    public PlayerRequestDTO generatePlayer() {
        return this
                .withRandomAge()
                .withRandomGender()
                .withRandomLogin()
                .withRandomRole()
                .withRandomPassword()
                .withRandomSecretName();
    }
}
