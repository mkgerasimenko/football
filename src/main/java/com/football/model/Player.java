package com.football.model;

import lombok.Data;

/**
 * Player payload.
 */
@Data
public abstract class Player {
    public String login;
    public String password;
    public String gender;
    public String role;
    public Integer age;
    public String screenName;
}
