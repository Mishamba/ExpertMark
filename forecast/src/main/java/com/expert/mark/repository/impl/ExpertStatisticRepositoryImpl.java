package com.expert.mark.repository.impl;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.LinkedList;
import java.util.List;

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

        return new ExpertStatistic(expertStatisticFuture.result());
    }

    @Override
    public ExpertStatistic updateExpertStatistic(ExpertStatistic expertStatistic) {
        JsonObject query = new JsonObject();
        query.put("_id", expertStatistic.getExpertUsername());
        JsonObject expertStatisticJson = expertStatistic.parseToJsonObject();
        expertStatisticJson.put("_id", expertStatistic.getExpertUsername());
        expertStatisticJson.remove("expertUsername");
        Future<JsonObject> updatedExpertStatisticJson = mongoClient.findOneAndUpdate(documentName, query, expertStatisticJson).onComplete(res -> {
            if (!res.succeeded()) {
                res.cause().printStackTrace();
            }
        });

        return new ExpertStatistic(updatedExpertStatisticJson.result());
    }

    @Override
    public List<ExpertStatistic> getUpdateRequiredExpertStatistics() {
        JsonObject query = new JsonObject();
        query.put("updateRequired", true);
        Future<List<JsonObject>> expertStatisticFuture = mongoClient.find(documentName, query);

        List<ExpertStatistic> expertStatistics = new LinkedList<>();
        expertStatisticFuture.onSuccess(res -> {
            for (JsonObject expertStatisticJson : res) {
                expertStatistics.add(new ExpertStatistic(expertStatisticJson));
            }
        });
        return expertStatistics;
    }

    @Override
    public void setUpdateRequired(String username) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject updateDate = new JsonObject();
        updateDate.put("updateRequired", true);
        mongoClient.findOneAndUpdate(documentName, query, updateDate);
    }
}
