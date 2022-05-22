package com.expert.mark.model.account;

import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private String username;
    private char[] password;
    //private String avatarPath;
    private Date createDate;
    private Role role;
    private Profile profile;

    public User(JsonObject jsonObject) {
        this.username = jsonObject.getString("username");
        this.password = jsonObject.getString("password").toCharArray();
        //this.avatarPath = jsonObject.getString("avatarPath");
        this.createDate = DateParser.parseToDate(jsonObject.getString("createDate"));
        this.role = Role.valueOf(jsonObject.getString("role"));
        this.profile = new Profile(jsonObject.getJsonObject("profile"));
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("username", this.username);
        if (this.password.length != 0) {
            jsonObject.put("password", this.password);
        }
        jsonObject.put("createDate", this.createDate);
        jsonObject.put("role", this.role.toString());
        if (this.profile != null) {
            jsonObject.put("profile", this.profile.parseToJson());
        }

        return jsonObject;
    }
}
