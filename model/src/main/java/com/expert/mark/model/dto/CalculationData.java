package com.expert.mark.model.dto;

import com.expert.mark.model.forecast.method.type.MethodType;
import io.vertx.core.json.JsonObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CalculationData {
    private JsonObject methodDataList;
    private MethodType methodType;
    private String quizId;

    public CalculationData(JsonObject jsonObject) {
        this.methodDataList = jsonObject.getJsonObject("methodDataList");
        this.methodType = MethodType.valueOf(jsonObject.getString("methodType"));
        this.quizId = jsonObject.getString("quizId");
    }
}
