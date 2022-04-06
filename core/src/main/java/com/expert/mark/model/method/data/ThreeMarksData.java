package com.expert.mark.model.method.data;

import com.expert.mark.model.method.MethodData;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
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
}
