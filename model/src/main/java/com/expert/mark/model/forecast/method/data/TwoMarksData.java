package com.expert.mark.model.forecast.method.data;

import com.expert.mark.model.forecast.method.MethodData;
import io.vertx.core.json.JsonObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class TwoMarksData extends MethodData {
    private double pessimisticMark;
    private double optimisticMark;
    private Double result;

    public TwoMarksData(JsonObject jsonObject) {
        this.pessimisticMark = jsonObject.getDouble("pessimisticMark");
        this.optimisticMark = jsonObject.getDouble("optimisticMark");
    }

    @Override
    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pessimisticMark", this.pessimisticMark);
        jsonObject.put("optimisticMark", this.optimisticMark);
        jsonObject.put("result", this.result);
        return jsonObject;
    }
}
