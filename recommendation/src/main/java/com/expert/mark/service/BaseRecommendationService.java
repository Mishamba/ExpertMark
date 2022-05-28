package com.expert.mark.service;

import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.repository.UserRepository;
import com.expert.mark.repository.impl.ForecastRepositoryImpl;
import com.expert.mark.repository.impl.UserRepositoryImpl;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class BaseRecommendationService {

    protected MongoClient mongoClient = DatabaseClientProvider.provide();
    protected ForecastRepository forecastRepository = new ForecastRepositoryImpl();
    protected UserRepository userRepository = new UserRepositoryImpl();

    protected Set<String> getAssetNamesForUser(String username) {
        Set<String> baseAssets = new HashSet<String>();
        forecastRepository.findUsersForecasts(username).forEach(forecast -> {
            baseAssets.add(forecast.getAssetName());
        });
        userRepository.getUserFollowings(username).forEach(expertUsername -> {
            forecastRepository.findUsersForecasts(expertUsername).forEach(forecast -> {
                baseAssets.add(forecast.getAssetName());
            });
        });
        baseAssets.addAll(this.getAssetNamesFromSpyDataForUser(username));

        return baseAssets;
    }

    protected List<String> getAssetNamesFromSpyDataForUser(String username) {
        List<String> assetNames = new LinkedList<>();
        JsonObject query = new JsonObject();
        query.put("_id", username);
        Future<JsonObject> userQueriesJsonFuture = mongoClient.findOne("userQuery", query, null);
        userQueriesJsonFuture.onSuccess(res -> {
            JsonArray queries = res.getJsonArray("queries");
            if (queries != null) {
                queries.forEach(x -> {
                    JsonObject queryData = (JsonObject) x;
                    String queryType = queryData.getString("queryType");
                    switch (queryType) {
                        case "forecastId":
                            assetNames.add(forecastRepository.findForecastById(queryData.getString("query")).getAssetName());
                            break;
                        case "username":
                            forecastRepository.findUsersForecasts(queryData.getString("query")).forEach(forecast -> {
                                assetNames.add(forecast.getAssetName());
                            });
                            break;
                        case "assetName":
                            assetNames.add(queryData.getString("queryData"));
                            break;
                    }
                });
            }
        });

        return assetNames;
    }
}
