package com.expert.mark.repository.impl;

import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import com.expert.mark.util.parser.DateParser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ForecastRepositoryImpl implements ForecastRepository {

    private final String forecastDocumentName = "forecast";
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public Forecast findForecastById(String id) {
        JsonObject query = new JsonObject();
        query.put("_id", id);
        List<Forecast> forecasts = findForecastsByQuery(query);
        return (forecasts.isEmpty()) ? null : forecasts.get(0);
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
    public List<Forecast> findUsersForecastsForAssetFromDate(String username, String assetName, Date date) {
        JsonObject query = new JsonObject();
        query.put("ownerUsername", username);
        query.put("assetName", assetName);
        query.put("date", new JsonObject().put("$gt", date));
        JsonObject dateQuery = new JsonObject();
        dateQuery.put("$lte", new Date());
        query.put("targetDate", dateQuery);
        return findForecastsByQuery(query);
    }

    @Override
    public List<Forecast> findUsersForecastsFromDate(String username, Date date) {
        JsonObject query = new JsonObject();
        query.put("ownerUsername", username);
        query.put("date", new JsonObject().put("$gt", date));
        JsonObject dateQuery = new JsonObject();
        dateQuery.put("$lte", new Date());
        query.put("targetDate", dateQuery);
        return findForecastsByQuery(query);
    }

    @Override
    public List<Forecast> findForecastsByTargetDate(Date date) {
        JsonObject query = new JsonObject();
        query.put("targetDate", DateParser.parseToString(date));
        return findForecastsByQuery(query);
    }

    @Override
    public List<Forecast> findForecastsForAssetWhereOwnerNotIn(String assetName, List<String> experts) {
        JsonObject query = new JsonObject();
        query.put("assetName", assetName);
        JsonArray ninExperts = new JsonArray();
        experts.forEach(ninExperts::add);
        query.put("ownerUsername", new JsonObject().put("$nin", ninExperts));
        return findForecastsByQuery(query);
    }

    @Override
    public List<Forecast> findForecastsForAsset(String assetName) {
        JsonObject query = new JsonObject();
        query.put("assetName", assetName);
        return findForecastsByQuery(query);
    }

    private List<Forecast> findForecastsByQuery(JsonObject query) {
        Future<List<JsonObject>> futureForecasts = mongoClient.
                find(forecastDocumentName, query);
        List<Forecast> forecasts = new LinkedList<>();
        futureForecasts.onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(jsonObject -> forecasts.add(new Forecast(jsonObject)));
            }
        }).onFailure(res -> {
            throw new RuntimeException(res.getMessage());
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return forecasts;
    }

    @Override
    public boolean deleteForecast(String id) {
        JsonObject query = new JsonObject();
        query.put("_id", id);
        AtomicReference<Boolean> success = new AtomicReference<>(false);
        mongoClient.findOneAndDelete(forecastDocumentName, query).onComplete(res -> {
            success.set(res.succeeded());
            if (!res.succeeded()) {
                res.cause().printStackTrace();
            }
        });

        return success.get();
    }

    @Override
    public Forecast createForecast(Forecast forecast) {
        JsonObject jsonForecast = forecast.parseToJson();
        jsonForecast.remove("_id");
        mongoClient.save(forecastDocumentName, jsonForecast, res -> {
            if (res.succeeded()) {
                forecast.set_id(res.result());
            }
        });



        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return forecast;
    }

    @Override
    public boolean updateForecast(Forecast forecast) {
        JsonObject query = new JsonObject();
        query.put("_id", forecast.get_id());
        JsonObject updateQuery = forecast.parseToJson();
        updateQuery.remove("creationDate");
        updateQuery.remove("assetName");
        updateQuery.remove("ownerUsername");
        AtomicReference<Boolean> successful = new AtomicReference<>(false);
        mongoClient.findOneAndUpdate(forecastDocumentName, query, updateQuery, res -> {
            successful.set(res.succeeded());
        });

        return successful.get();
    }
}
