package com.expert.mark.model.account;

import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonArray;
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
        this.password =  jsonObject.getString("password").toCharArray();
        //this.avatarPath = jsonObject.getString("avatarPath");
        this.createDate = DateParser.parseToDateWithMinutes(jsonObject.getString("createDate"));
        String roleString = jsonObject.getString("role");
        if (!roleString.isEmpty()) {
            this.role = Role.valueOf(roleString);
        } else {
            this.role = Role.USER;
        }
        JsonObject jsonProfile = jsonObject.getJsonObject("profile");
        if (jsonProfile != null) {
            this.profile = new Profile(jsonProfile);
        }
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("username", this.username);
        if (this.password.length != 0) {
            JsonArray passwordJson = new JsonArray();
            for (char symbol : this.password) {
                passwordJson.add(String.valueOf(symbol));
            }
            jsonObject.put("password", passwordJson);
        }
        jsonObject.put("createDate", DateParser.parseToString(this.createDate));
        jsonObject.put("role", this.role.name());
        if (this.profile != null) {
            jsonObject.put("profile", this.profile.parseToJson());
        }

        return jsonObject;
    }
}
