package com.expert.mark.model.account.expert;

import com.expert.mark.model.account.Character;
import io.vertx.core.json.JsonObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ExpertStatistic {
    private String expertUsername;
    private Character character;
    private Float accuracy;

    public ExpertStatistic(JsonObject jsonObject) {
        this.expertUsername = jsonObject.getString("expertUsername");
        this.character = Character.valueOf(jsonObject.getString("character"));
        this.accuracy = jsonObject.getFloat("accuracy");
    }

    public JsonObject parseToJsonObject() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("expertUsername", this.expertUsername);
        jsonObject.put("character", this.character.name());
        jsonObject.put("accuracy", this.accuracy);

        return jsonObject;
    }
}
