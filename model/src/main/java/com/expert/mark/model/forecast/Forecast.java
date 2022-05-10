package com.expert.mark.model.forecast;

import com.expert.mark.model.forecast.method.MethodData;
import com.expert.mark.model.forecast.method.type.MethodType;
import com.expert.mark.model.forecast.status.ForecastStatus;
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
    private String id;
    private MethodType methodType;
    private MethodData methodData;
    private String assetName;
    private String ownerUsername;
    private Date createDate;
    private Date targetDate;
    private ForecastStatus forecastStatus;

    public Forecast(JsonObject jsonObject) {
        this.id = jsonObject.getString("_id");
        this.methodType = MethodType.valueOf(jsonObject.getString("methodType"));
        this.methodData = MethodDataParser.
                parseJsonToMethodData(jsonObject.getJsonObject("methodDate"), this.methodType);
        this.assetName = jsonObject.getString("assetName");
        this.ownerUsername = jsonObject.getString("ownerUsername");
        this.createDate = DateParser.parseToDate(jsonObject.getString("createDate"));
        this.targetDate = DateParser.parseToDate(jsonObject.getString("targetDate"));
        this.forecastStatus = ForecastStatus.valueOf(jsonObject.getString("forecastStatus"));
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("_id", this.id);
        jsonObject.put("methodType", this.methodType.name());
        jsonObject.put("methodData", this.methodData.parseToJson());
        jsonObject.put("assetName", this.assetName);
        jsonObject.put("ownerUsername", this.ownerUsername);
        jsonObject.put("createDate", this.createDate.toString());
        jsonObject.put("targetDate", this.targetDate.toString());
        jsonObject.put("forecastStatus", this.forecastStatus.name());
        return jsonObject;
    }
}
