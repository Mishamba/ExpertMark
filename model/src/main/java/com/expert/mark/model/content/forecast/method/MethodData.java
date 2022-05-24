package com.expert.mark.model.content.forecast.method;

import io.vertx.core.json.JsonObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class MethodData {
    protected Float result;
    public abstract JsonObject parseToJson();
}
