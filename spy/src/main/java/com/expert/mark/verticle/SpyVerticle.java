package com.expert.mark.verticle;

import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class SpyVerticle extends AbstractVerticle {

    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public void start() throws Exception {
        getVertx().eventBus().consumer("userQuery", message -> {
            JsonObject userQuery = (JsonObject) message.body();
            JsonObject findQuery = new JsonObject();
            findQuery.put("_id", userQuery.getString("username"));
            JsonObject updateQuery = new JsonObject();
            updateQuery.put("$push", new JsonObject().put("queries", userQuery.getString("queryData")));
            mongoClient.findOneAndUpdate("userQuery", findQuery, updateQuery, res -> {
                if (res.failed()) {
                    userQuery.put("_id", userQuery.getString("username"));
                    updateQuery.remove("username");
                    mongoClient.save("userQuery", userQuery);
                }
            });
        });
    }
}
