package com.expert.mark.repository.impl;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.concurrent.atomic.AtomicReference;

public class ExpertStatisticRepositoryImpl implements ExpertStatisticRepository {

    private final String documentName = "expert_statistic";
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
    public ExpertStatistic saveExpertStatistic(ExpertStatistic expertStatistic) {
        JsonObject expertStatisticJson = expertStatistic.parseToJsonObject();
        expertStatisticJson.put("_id", expertStatistic.getExpertUsername());
        expertStatisticJson.remove("expertUsername");
        AtomicReference<ExpertStatistic> savedExpertStatistic = new AtomicReference<>();
        mongoClient.save(documentName, expertStatisticJson).onComplete(res -> {
            if (!res.succeeded()) {
                res.cause().printStackTrace();
            } else {
                savedExpertStatistic.set(new ExpertStatistic(mongoClient.findOne(documentName,
                        new JsonObject().put("_id", res.result()), null).result()));
            }
        });

        return savedExpertStatistic.get();
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
    public ExpertStatistic deleteExpertStatisticByExpertUsername(String expertUsername) {
        JsonObject query = new JsonObject();
        query.put("_id", expertUsername);
        Future<JsonObject> deletedExpertStatisticJson = mongoClient.findOneAndDelete(documentName, query).onComplete(res -> {
            if (!res.succeeded()) {
                res.cause().printStackTrace();
            }
        });

        return new ExpertStatistic(deletedExpertStatisticJson.result());

    }
}
