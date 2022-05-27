package com.expert.mark.model.content.forecast.method;

import io.vertx.core.json.JsonObject;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class MethodData {
    public abstract JsonObject parseToJson();
    public abstract double getResult();
}
