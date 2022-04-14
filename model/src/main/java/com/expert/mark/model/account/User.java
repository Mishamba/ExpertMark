package com.expert.mark.model.account;

import io.vertx.core.json.JsonObject;

import java.util.Date;

public class User {
    private String username;
    private char[] password;
    private String avatarPath;
    private Date createDate;
    private Date lastUpdateDate;
    private String role;
    private Profile profile;

    public User(JsonObject jsonObject) {

    }
}
