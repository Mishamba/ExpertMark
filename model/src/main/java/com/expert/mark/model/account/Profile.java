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
    private List<Achievement> achievementList;
    private List<String> followingUserNames;

    public Profile(JsonObject jsonObject) {
        this.achievementList = new ArrayList<>();
        JsonArray achievementsJsonArray = jsonObject.getJsonArray("achievementList");
        achievementsJsonArray.forEach(achievementJsonObject -> {
            this.achievementList.add(new Achievement((JsonObject) achievementJsonObject));
        });

        this.followingUserNames = new ArrayList<>();
        JsonArray followingJsonArray = jsonObject.getJsonArray("following");
        followingJsonArray.forEach(username -> {
            this.followingUserNames.add((String) username);
        });
    }
}
