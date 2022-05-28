package com.expert.mark.controller;

import com.expert.mark.model.account.User;
import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.ExpertRecommendationService;
import com.expert.mark.service.ForecastRecommendationService;
import com.expert.mark.service.impl.ExpertRecommendationServiceImpl;
import com.expert.mark.service.impl.ForecastRecommendationServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class RecommendationController extends AbstractVerticle {

    private final ExpertRecommendationService expertRecommendationService = new ExpertRecommendationServiceImpl();
    private final ForecastRecommendationService forecastRecommendationService = new ForecastRecommendationServiceImpl();

    @Override
    public void start() throws Exception {
        Router router = Router.router(Vertx.vertx());
        router.get("/recommend/experts").handler(this::findExpertsToRecommend);
        router.get("/recommend/forecasts").handler(this::findForecastsToRecommend);

        vertx.createHttpServer().requestHandler(router).listen(8083).onFailure(Throwable::printStackTrace);
    }

    private void findForecastsToRecommend(RoutingContext routingContext) {
        String username = routingContext.getBodyAsJson().getString("username");
        List<Forecast> forecasts = forecastRecommendationService.recommendForecastForUser(username);
        JsonArray forecastsJson = new JsonArray();
        forecasts.forEach(forecast -> forecastsJson.add(forecast.parseToJson()));

        routingContext.response().setStatusCode(200).putHeader("Content-Type", "application/json").end(forecastsJson.encode());
    }

    private void findExpertsToRecommend(RoutingContext routingContext) {
        String username = routingContext.getBodyAsJson().getString("username");
        List<User> experts = expertRecommendationService.recommendExpertsForUser(username);
        JsonArray expertsJson = new JsonArray();
        experts.forEach(forecast -> expertsJson.add(forecast.parseToJson()));

        routingContext.response().setStatusCode(200).putHeader("Content-Type", "application/json").end(expertsJson.encode());
    }
}
