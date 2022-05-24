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
    private Date lastUpdateDate;
    private Boolean requiresUpdate;

    public ExpertStatistic(JsonObject jsonObject) {
        this.expertUsername = jsonObject.getString("expertUsername");
        this.character = Character.valueOf(jsonObject.getString("character"));
        this.accuracy = jsonObject.getFloat("accuracy");
        this.lastUpdateDate = DateParser.parseToDate(jsonObject.getString("lastUpdateDate"));
        this.requiresUpdate = jsonObject.getBoolean("requiresUpdate");
    }

    public JsonObject parseToJsonObject() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("expertUsername", this.expertUsername);
        jsonObject.put("character", this.character.name());
        jsonObject.put("accuracy", this.accuracy);
        jsonObject.put("lastUpdateDate", DateParser.parseToString(this.lastUpdateDate));
        jsonObject.put("requiresUpdate", this.requiresUpdate);

        return jsonObject;
    }
}
