package com.expert.mark.model.content.forecast;

import com.expert.mark.model.content.forecast.method.MethodData;
import com.expert.mark.model.content.forecast.method.type.MethodType;
import com.expert.mark.model.parser.MethodDataParser;
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
public class Forecast {
    private String _id;
    private MethodType methodType;
    private MethodData methodData;
    private String assetName;
    private String ownerUsername;
    private Date createDate;
    private Date targetDate;
    private Float accuracy;

    public Forecast(JsonObject jsonObject) {
        this._id = jsonObject.getString("_id");
        this.methodType = MethodType.valueOf(jsonObject.getString("methodType"));
        this.methodData = MethodDataParser.
                parseJsonToMethodData(jsonObject.getJsonObject("methodData"), this.methodType);
        this.assetName = jsonObject.getString("assetName");
        this.ownerUsername = jsonObject.getString("ownerUsername");
        this.createDate = DateParser.parseToDateWithoutMinutes(jsonObject.getString("createDate"));
        this.targetDate = DateParser.parseToDateWithoutMinutes(jsonObject.getString("targetDate"));
        this.accuracy = jsonObject.getFloat("accuracy");
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("_id", this._id);
        jsonObject.put("methodType", this.methodType.name());
        jsonObject.put("methodData", this.methodData.parseToJson());
        jsonObject.put("assetName", this.assetName);
        jsonObject.put("ownerUsername", this.ownerUsername);
        jsonObject.put("createDate", DateParser.parseToStringWithoutMinutes(this.createDate));
        jsonObject.put("targetDate", DateParser.parseToStringWithoutMinutes(this.targetDate));
        jsonObject.put("accuracy", this.accuracy);
        return jsonObject;
    }
}
