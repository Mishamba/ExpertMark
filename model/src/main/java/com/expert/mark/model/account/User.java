package com.expert.mark.model.account;

import io.vertx.core.json.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String username;
    private char[] password;
    private String avatarPath;
    private Date createDate;
    private Date lastUpdateDate;
    private Role role;
    private Profile profile;

    public User(JsonObject jsonObject) {
        this.username = jsonObject.getString("username");
        this.password = jsonObject.getString("password").toCharArray();
        this.avatarPath = jsonObject.getString("avatarPath");
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy zzzz");
            this.createDate = dateFormat.parse(jsonObject.getString("createDate"));
            this.lastUpdateDate = dateFormat.parse(jsonObject.getString("lastUpdateDate"));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Wrong date format");
        }
        this.role = Role.valueOf(jsonObject.getString("role"));
        this.profile = new Profile(jsonObject.getJsonObject("profile"));
    }
}
