package com.expert.mark.util.db;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class DatabaseClientProvider {
    public static MongoClient provide() {
        JsonObject config = new JsonObject();
        config.put("username", "TODO");
        config.put("password", "TODO");
        config.put("db_name", "expertMark");
        return MongoClient.create(Vertx.vertx(), config);
    }
}
