package com.expert.mark.service;

import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.repository.UserRepository;
import com.expert.mark.repository.impl.ForecastRepositoryImpl;
import com.expert.mark.repository.impl.UserRepositoryImpl;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseRecommendationService {

    protected MongoClient mongoClient = DatabaseClientProvider.provide();
    protected ForecastRepository forecastRepository = new ForecastRepositoryImpl();
    protected UserRepository userRepository = new UserRepositoryImpl();

    protected List<String> getAssetNamesForUser(String username) {
        List<String> baseAssets = new LinkedList<String>();
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
        Future<JsonObject> userQueriesJsonFuture = mongoClient.findOne("spyData", query, null);
        userQueriesJsonFuture.onSuccess(res -> {
            res.getJsonArray("queries").forEach(x -> {
                JsonObject queryData = (JsonObject) x;
                String queryType = queryData.getString("queryType");
                switch (queryType) {
                    case "forecastId" :
                        assetNames.add(forecastRepository.findForecastById(queryData.getString("query")).getAssetName());
                        break;
                    case "username" :
                        forecastRepository.findUsersForecasts(queryData.getString("query")).forEach(forecast -> {
                            assetNames.add(forecast.getAssetName());
                        });
                        break;
                    case "assetName" :
                        assetNames.add(queryData.getString("queryData"));
                        break;
                }
            });
        });

        return assetNames;
    }
}
