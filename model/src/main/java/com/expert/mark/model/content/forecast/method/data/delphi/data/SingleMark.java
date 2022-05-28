package com.expert.mark.model.content.forecast.method.data.delphi.data;

import com.expert.mark.model.content.forecast.method.MethodData;
import com.expert.mark.model.content.forecast.method.type.MethodType;
import com.expert.mark.model.parser.MethodDataParser;
import io.vertx.core.json.JsonObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SingleMark implements Comparable<SingleMark> {
    private MethodType methodType;
    private MethodData mark;
    private String ownerUsername;

    public SingleMark(JsonObject jsonObject) {
        this.methodType = MethodType.valueOf(jsonObject.getString("methodType"));
        this.mark = MethodDataParser.parseJsonToMethodData(jsonObject.getJsonObject("methodData"), this.methodType);
        this.ownerUsername = jsonObject.getString("ownerUsername");
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("methodType", this.methodType.name());
        jsonObject.put("methodData", this.mark.parseToJson());
        jsonObject.put("ownerUsername", this.ownerUsername);

        return jsonObject;
    }

    @Override
    public int compareTo(SingleMark o) {
        return Double.compare(this.mark.getResult(), o.getMark().getResult());
    }
}
