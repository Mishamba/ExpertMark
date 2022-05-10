package com.expert.mark.util.db;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class DatabaseClientProvider {
    public static MongoClient provide() {
        JsonObject config = new JsonObject();
        config.put("username", "client");
        config.put("password", "123");
        config.put("db_name", "expertMark");
        config.put("host", "localhost");
        config.put("port", 27017);
        return MongoClient.create(Vertx.vertx(), config);
    }
}
