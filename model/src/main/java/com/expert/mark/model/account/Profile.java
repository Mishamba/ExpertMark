package com.expert.mark.model.account;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
        this.followingUserNames = new ArrayList<>();
        JsonArray followingJsonArray = jsonObject.getJsonArray("following");
        followingJsonArray.forEach(username -> {
            this.followingUserNames.add((String) username);
        });

        this.accountDescription = jsonObject.getString("accountDescription");
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();

        JsonArray followingUserNamesJson = new JsonArray();
        followingUserNames.forEach(followingUserNamesJson::add);
        jsonObject.put("followingUserNames", followingUserNamesJson);
        jsonObject.put("accountDescription", this.accountDescription);

        return jsonObject;
    }
}
