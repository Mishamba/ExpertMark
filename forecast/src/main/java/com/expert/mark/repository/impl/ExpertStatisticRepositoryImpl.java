package com.expert.mark.repository.impl;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ExpertStatisticRepositoryImpl implements ExpertStatisticRepository {

    private final String documentName = "expertStatistic";
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public ExpertStatistic getExpertStatisticByExpertUsername(String expertUsername) {
        JsonObject query = new JsonObject();
        query.put("_id", expertUsername);
        Future<JsonObject> expertStatisticFuture = mongoClient.findOne(documentName, query, null).
                onComplete(res -> {
                    if (!res.succeeded()) {
                        res.cause().printStackTrace();
                    }
                });

        AtomicReference<ExpertStatistic> expertStatistic = new AtomicReference<>();
        expertStatisticFuture.onSuccess(res -> {
            res.put("expertUsername", res.getString("_id"));
            expertStatistic.set(new ExpertStatistic(res));
        });
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return expertStatistic.get();
    }

    @Override
    public ExpertStatistic updateExpertStatistic(ExpertStatistic expertStatistic) {
        JsonObject query = new JsonObject();
        query.put("_id", expertStatistic.getExpertUsername());
        JsonObject expertStatisticJson = expertStatistic.parseToJsonObject();
        expertStatisticJson.put("_id", expertStatistic.getExpertUsername());
        expertStatisticJson.remove("expertUsername");
        mongoClient.save(documentName, expertStatisticJson);

        return expertStatistic;
    }

    @Override
    public ExpertStatistic createExpertStatistic(ExpertStatistic expertStatistic) {
        JsonObject expertStatisticJson = expertStatistic.parseToJsonObject();
        expertStatisticJson.put("_id", expertStatistic.getExpertUsername());
        expertStatisticJson.remove("expertUsername");

        if (this.getExpertStatisticByExpertUsername(expertStatistic.getExpertUsername()) == null) {
            mongoClient.insert(documentName, expertStatisticJson);
        }

        return expertStatistic;
    }

    @Override
    public List<ExpertStatistic> getUpdateRequiredExpertStatistics() {
        JsonObject query = new JsonObject();
        query.put("requiresUpdate", true);
        Future<List<JsonObject>> expertStatisticFuture = mongoClient.find(documentName, query);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<ExpertStatistic> expertStatistics = new LinkedList<>();
        expertStatisticFuture.onSuccess(res -> {
            for (JsonObject expertStatisticJson : res) {
                expertStatistics.add(new ExpertStatistic(expertStatisticJson));
            }
        });
        return expertStatistics;
    }

    @Override
    public void setUpdateRequired(String username, boolean updateRequired) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject updateDate = new JsonObject();
        updateDate.put("$set", new JsonObject().put("requiresUpdate", updateRequired));
        mongoClient.findOneAndUpdate(documentName, query, updateDate).
                onFailure(res -> res.printStackTrace());
    }
}
