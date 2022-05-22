package com.expert.mark.model.content.forecast.method.data;

import com.expert.mark.model.content.forecast.method.MethodData;
import io.vertx.core.json.JsonObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ThreeMarksWithChancesData extends MethodData {
    private double pessimisticMark;
    private float pessimisticChance;
    private double realisticMark;
    private float realisticChance;
    private double optimisticMark;
    private float optimisticChance;
    private Double result;

    public ThreeMarksWithChancesData(JsonObject jsonObject) {
        this.pessimisticMark = jsonObject.getDouble("pessimisticMark");
        this.pessimisticChance = jsonObject.getFloat("pessimisticChance");
        this.realisticMark = jsonObject.getDouble("realisticMark");
        this.realisticChance = jsonObject.getFloat("realisticMark");
        this.optimisticMark = jsonObject.getDouble("optimisticMark");
        this.optimisticChance = jsonObject.getFloat("optimisticChance");
    }

    @Override
    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pessimisticMark", this.pessimisticMark);
        jsonObject.put("pessimisticChance", this.pessimisticChance);
        jsonObject.put("realisticMark", this.realisticMark);
        jsonObject.put("realisticChance", this.realisticChance);
        jsonObject.put("optimisticMark", this.optimisticMark);
        jsonObject.put("optimisticChance", this.optimisticChance);
        jsonObject.put("result", this.result);
        return jsonObject;
    }
}
