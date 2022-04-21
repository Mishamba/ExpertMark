package com.expert.mark.repository.impl;

import com.expert.mark.model.forecast.Forecast;
import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.bson.Document;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ForecastRepositoryImpl implements ForecastRepository {

    private final String documentName = "Forecast";
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public Forecast findForecastById(String id) {
        JsonObject query = new JsonObject();
        query.put("_id", id);
        return findForecastsByQuery(query).get(0);
    }

    @Override
    public List<Forecast> findUsersForecasts(String username) {
        JsonObject query = new JsonObject();
        query.put("ownerUsername", username);
        return findForecastsByQuery(query);
    }

    @Override
    public List<Forecast> findUsersForecastsForAsset(String username, String assetName) {
        JsonObject query = new JsonObject();
        query.put("ownerUsername", username);
        query.put("assetName", assetName);
        return findForecastsByQuery(query);
    }

    @Override
    public List<Forecast> findUsersForecastsForAssetFromCurrentDate(String username, String assetName) {
        JsonObject query = new JsonObject();
        query.put("ownerUsername", username);
        query.put("assetName", assetName);
        JsonObject dateQuery = new JsonObject();
        Document document = new Document();
        document.put("date", new Date());
        dateQuery.put("$lte", document.getString("date"));
        query.put("targetDate", dateQuery);
        return findForecastsByQuery(query);
    }

    private List<Forecast> findForecastsByQuery(JsonObject query) {
        Future<List<JsonObject>> futureForecasts = mongoClient.
                find(documentName, query);
        List<Forecast> forecasts = new LinkedList<>();
        futureForecasts.onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(jsonObject -> forecasts.add(new Forecast(jsonObject)));
            }
        }).onFailure(res -> {
            throw new RuntimeException(res.getMessage());
        });

        return forecasts;

    }

    @Override
    public boolean deleteForecast(String id) {
        JsonObject query = new JsonObject();
        query.put("_id", id);
        AtomicReference<Boolean> success = new AtomicReference<>(false);
        mongoClient.findOneAndDelete(documentName, query).onComplete(res -> {
            success.set(res.succeeded());
        });

        return success.get();
    }

    @Override
    public Forecast createForecast(Forecast forecast) {
        JsonObject jsonForecast = forecast.parseToJson();
        jsonForecast.remove("_id");
        mongoClient.save(documentName, jsonForecast, res -> {
            if (res.succeeded()) {
                forecast.setId(res.result());
            }
        });

        return forecast;
    }

    @Override
    public boolean updateForecast(Forecast forecast) {
        JsonObject query = new JsonObject();
        query.put("_id", forecast.getId());
        AtomicReference<Boolean> successful = new AtomicReference<>(false);
        mongoClient.findOneAndUpdate(documentName, query, forecast.parseToJson(), res -> {
            successful.set(res.succeeded());
        });

        return successful.get();
    }
}
