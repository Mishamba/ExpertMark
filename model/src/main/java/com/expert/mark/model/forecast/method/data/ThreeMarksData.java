package com.expert.mark.model.forecast.method.data;

import com.expert.mark.model.forecast.method.MethodData;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ThreeMarksData extends MethodData {
    private double pessimisticMark;
    private double realisticMark;
    private double optimisticMark;
    private Double result;

    public ThreeMarksData(@NotNull JsonObject jsonObject) {
        this.pessimisticMark = jsonObject.getDouble("pessimisticMark");
        this.realisticMark = jsonObject.getDouble("realisticMark");
        this.optimisticMark = jsonObject.getDouble("optimisticMark");
    }

    @Override
    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pessimisticMark", this.pessimisticMark);
        jsonObject.put("realisticMark", this.realisticMark);
        jsonObject.put("optimisticMark", this.optimisticMark);
        jsonObject.put("result", this.result);
        return jsonObject;
    }
}
