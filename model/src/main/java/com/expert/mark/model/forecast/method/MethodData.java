package com.expert.mark.model.forecast.method;

import io.vertx.core.json.JsonObject;

public abstract class MethodData {
    public abstract JsonObject parseToJson();
}
