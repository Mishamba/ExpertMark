package com.expert.mark.model.account;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Profile {
    private List<String> followingUserNames;
    private String accountDescription;

    public Profile(JsonObject jsonObject) {
        JsonArray followingJsonArray = jsonObject.getJsonArray("following");
        if (followingJsonArray != null) {
            this.followingUserNames = new ArrayList<>();
            followingJsonArray.forEach(username -> {
                this.followingUserNames.add((String) username);
            });
        }

        this.accountDescription = jsonObject.getString("accountDescription");
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();

        JsonArray followingUserNamesJson = new JsonArray();
        if (this.followingUserNames != null) {
            followingUserNames.forEach(followingUserNamesJson::add);
        }
        jsonObject.put("followingUserNames", followingUserNamesJson);
        jsonObject.put("accountDescription", this.accountDescription);

        return jsonObject;
    }
}
