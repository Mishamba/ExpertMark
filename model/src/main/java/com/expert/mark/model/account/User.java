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
    private String password;
    private Date createDate;
    private Role role;
    private Profile profile;

    public User(JsonObject jsonObject) {
        this.username = jsonObject.getString("username");
        if (jsonObject.getString("password") != null) {
            this.password = jsonObject.getString("password");
        }
        this.createDate = DateParser.parseToDateWithoutMinutes(jsonObject.getString("createDate"));
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
        if (this.password != null) {
            jsonObject.put("password", this.password);
        }
        jsonObject.put("createDate", DateParser.parseToStringWithoutMinutes(this.createDate));
        jsonObject.put("role", this.role.name());
        if (this.profile != null) {
            jsonObject.put("profile", this.profile.parseToJson());
        }

        return jsonObject;
    }
}
