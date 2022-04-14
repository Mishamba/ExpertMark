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
}
