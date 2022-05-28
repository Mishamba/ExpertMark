package com.expert.mark.model.account.expert;

import com.expert.mark.model.account.Character;
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
public class ExpertStatistic {
    private String expertUsername;
    private Character character;
    private Float accuracy;

    public ExpertStatistic(JsonObject jsonObject) {
        this.expertUsername = jsonObject.getString("expertUsername");
        if (jsonObject.getString("character") != null) {
            this.character = Character.valueOf(jsonObject.getString("character"));
        }
        this.accuracy = jsonObject.getFloat("accuracy");
    }

    public JsonObject parseToJsonObject() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("expertUsername", this.expertUsername);
        if (this.character != null) {
            jsonObject.put("character", this.character.name());
        } else {
            jsonObject.put("character", null);
        }
        jsonObject.put("accuracy", this.accuracy);

        return jsonObject;
    }
}
