package com.expert.mark.verticle;

import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
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
            updateQuery.put("$push", new JsonObject().put("queries", userQuery.getJsonObject("queryData")));
            mongoClient.find("userQuery", findQuery).onSuccess(res -> {

                if (!res.isEmpty()) {
                    mongoClient.findOneAndUpdate("userQuery", findQuery, updateQuery).
                            onComplete(yes -> System.out.println("is spy save completed: " + yes.result()));
                } else {
                    userQuery.put("_id", userQuery.getString("username"));
                    userQuery.remove("username");
                    userQuery.put("queries", new JsonArray().add(userQuery.getJsonObject("queryData")));
                    userQuery.remove("queryData");
                    mongoClient.save("userQuery", userQuery);
                }
            });
        });
    }
}
