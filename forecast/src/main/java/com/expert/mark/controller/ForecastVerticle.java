package com.expert.mark.controller;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.ForecastService;
import com.expert.mark.service.ForecastProcessor;
import com.expert.mark.service.impl.ForecastProcessorImpl;
import com.expert.mark.service.impl.ForecastServiceImpl;
import com.expert.mark.util.security.decryption.DecryptionUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ForecastVerticle extends AbstractVerticle {

    private final ForecastService forecastService = new ForecastServiceImpl();
    private final ForecastProcessor forecastProcessor = new ForecastProcessorImpl();
    private final Logger logger = LoggerFactory.getLogger(ForecastVerticle.class.getName());

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(ctx -> {
            logger.debug("request to url {}", ctx.request().uri());
        });
        router.delete("/forecasts/delete").handler(this::deleteForecast);
        router.get("/forecasts/user_following_based/:assetName").handler(this::userFollowingBasedAssetForecast);
        router.get("/forecasts/user_owned/:username").handler(this::getUsersForecasts);
        router.get("/forecasts/assets/:assetName").handler(this::getAssetForecasts);
        router.put("/forecasts/create").handler(this::createForecast);//tested
        router.put("/forecasts/update").handler(this::updateForecast);
        router.get("/forecasts/:id").handler(this::getForecastById);//tested
        router.patch("/processExpertStatistic").handler(this::processForecastsAndExpertStatisticByCall);
        router.get("/expertStatistic/:expertUsername").handler(this::getExpertStatistic);

        vertx.createHttpServer().requestHandler(router).listen(8081).onFailure(Throwable::printStackTrace);

        vertx.setPeriodic(86400 * 1000, this::processForecastsAndExpertStatistic);
    }

    private void getExpertStatistic(RoutingContext routingContext) {
        String expertUsername = routingContext.pathParam("expertUsername");
        ExpertStatistic expertStatistic = forecastService.getExpertStatisticByExpertUsername(expertUsername);
        routingContext.response().setStatusCode(200).putHeader("Content-Type", "application/json").
                send(expertStatistic.parseToJsonObject().encode());
    }

    private void processForecastsAndExpertStatisticByCall(RoutingContext routingContext) {
        forecastProcessor.updateExpertStatisticsAndCalculateForecastAccuracy();
        routingContext.response().send();
    }

    private void processForecastsAndExpertStatistic(Long aLong) {
        forecastProcessor.updateExpertStatisticsAndCalculateForecastAccuracy();
    }

    void getForecastById(RoutingContext ctx) {
        String forecastId = ctx.pathParam("id");
        String actorUsername = ctx.getBodyAsJson().getString("username");
        this.sendQueryToSpy(actorUsername, forecastId, "forecastId");
        Forecast forecast = forecastService.getForecastById(forecastId);
        ctx.response().putHeader("Content-Type", "application/json").send((forecast == null) ? "{}" : forecast.parseToJson().encode());
    }

    void updateForecast(RoutingContext ctx) {
        JsonObject updatedForecast = ctx.getBodyAsJson();
        Forecast forecastToUpdate = new Forecast(updatedForecast);
        boolean updatedSuccessfully = forecastService.updateForecast(forecastToUpdate);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((updatedSuccessfully) ? 200 : 500).send(forecastToUpdate.parseToJson().encode());
    }

    void createForecast(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        Forecast forecast = new Forecast(body.getJsonObject("forecast"));
        String username = body.getString("username");
        this.sendQueryToSpy(username, forecast.getAssetName(), "assetName");
        Forecast savedForecast = forecastService.createForecast(forecast);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((savedForecast.get_id() != null && !savedForecast.get_id().isEmpty()) ? 200 : 500).
                send(savedForecast.parseToJson().encode());
    }

    void deleteForecast(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String id = body.getString("_id");
        boolean deletedSuccessfully = forecastService.deleteForecast(id);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((deletedSuccessfully) ? 200 : 500).send((deletedSuccessfully) ? "Forecast was deleted" : "Forecast wasn't deleted");
    }

    void getUsersForecasts(RoutingContext ctx) {
        String username = ctx.pathParam("usernameToFind");
        String actorUsername = ctx.getBodyAsJson().getString("username");
        this.sendQueryToSpy(actorUsername, username, "username");
        List<Forecast> forecastList = forecastService.getUserForecasts(username);
        JsonArray forecastListJson = new JsonArray();
        forecastList.forEach(x -> forecastListJson.add(x.parseToJson()));
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode(200).send(forecastListJson.encode());
    }

    void getAssetForecasts(RoutingContext ctx) {
        String assetName = ctx.pathParam("assetName");
        String actorUsername = ctx.getBodyAsJson().getString("username");
        this.sendQueryToSpy(actorUsername, assetName, "assetName");
        List<Forecast> forecastList = forecastService.getAssetForecasts(assetName, actorUsername);
        JsonArray forecastListJson = new JsonArray();
        forecastList.forEach(x -> forecastListJson.add(x.parseToJson()));
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode(200).send(forecastListJson.encode());
    }

    void userFollowingBasedAssetForecast(RoutingContext ctx) {
        String assetName = ctx.pathParam("assetName");
        String userToken = ctx.request().getCookie("userToken").getValue();
        String username = null;
        if (userToken != null) {
            username = DecryptionUtil.getUsernameFromUserToken(userToken);
        }
        List<Forecast> forecastList = forecastService.getAssetForecasts(assetName, username);
        JsonArray forecastListJson = new JsonArray();
        forecastList.forEach(x -> forecastListJson.add(x.parseToJson()));
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode(200).send(forecastListJson.encode());
    }

    private void sendQueryToSpy(String username, String query, String queryType) {
        vertx.eventBus().
                send("userQuery", new JsonObject().
                        put("username", username).
                        put("queryData", new JsonObject().
                                put("query", query).
                                put("queryType", queryType)));
    }
}
