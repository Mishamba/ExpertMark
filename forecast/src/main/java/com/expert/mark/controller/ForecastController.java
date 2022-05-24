package com.expert.mark.controller;

import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.ForecastService;
import com.expert.mark.service.Processor;
import com.expert.mark.service.impl.ProcessorImpl;
import com.expert.mark.service.impl.ForecastServiceImpl;
import com.expert.mark.util.security.decryption.DecryptionUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;

public class ForecastController extends AbstractVerticle {

    private final ForecastService forecastService = new ForecastServiceImpl();
    private final Processor processor = new ProcessorImpl();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.delete("/forecasts/delete").handler(this::deleteForecast);
        router.get("/forecasts/user_following_based/:assetName").handler(this::userFollowingBasedAssetForecast);
        router.get("/forecasts/user_owned/:username").handler(this::getUsersForecasts);
        router.get("/forecasts/assets/:asset_name").handler(this::getAssetForecasts);
        router.put("/forecasts/create").handler(this::createForecast);
        router.put("/forecasts/update").handler(this::updateForecast);
        router.get("/forecasts/:id").handler(this::getForecastById);
        router.patch("/processExpertStatistic").handler(this::processForecastsAndExpertStatisticByCall);

        vertx.createHttpServer().requestHandler(router).listen(8081).onFailure(Throwable::printStackTrace);

        vertx.setPeriodic(86400, this::processForecastsAndExpertStatistic);
    }

    private void processForecastsAndExpertStatisticByCall(RoutingContext routingContext) {
        processor.updateExpertStatisticsAndCalculateForecastAccuracy();
    }

    private void processForecastsAndExpertStatistic(Long aLong) {
        processor.updateExpertStatisticsAndCalculateForecastAccuracy();
    }

    void getForecastById(RoutingContext ctx) {
        String forecastId = ctx.pathParam("id");
        Forecast forecast = forecastService.getForecastById(forecastId);
        ctx.response().putHeader("Content-Type", "application/json").send((forecast == null) ? "{}" : forecast.toString());
    }

    void updateForecast(RoutingContext ctx) {
        JsonObject updatedForecast = ctx.getBodyAsJson();
        Forecast forecastToUpdate = new Forecast(updatedForecast);
        boolean updatedSuccessfully = forecastService.updateForecast(forecastToUpdate);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((updatedSuccessfully) ? 200 : 500).send(forecastToUpdate.toString());
    }

    void createForecast(RoutingContext ctx) {
        JsonObject jsonForecast = ctx.getBodyAsJson();
        Forecast forecast = new Forecast(jsonForecast);
        Forecast savedForecast = forecastService.createForecast(forecast);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((savedForecast.get_id() != null && !savedForecast.get_id().isEmpty()) ? 200 : 500).
                send(savedForecast.toString());
    }

    void deleteForecast(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String id = body.getString("_id");
        boolean deletedSuccessfully = forecastService.deleteForecast(id);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((deletedSuccessfully) ? 200 : 500).send((deletedSuccessfully) ? "Forecast was deleted" : "Forecast wasn't deleted");
    }

    void getUsersForecasts(RoutingContext ctx) {
        String username = ctx.pathParam("username");
        List<Forecast> forecastList = forecastService.getUserForecasts(username);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode(200).send(forecastList.toString());
    }

    void getAssetForecasts(RoutingContext ctx) {
        String assetName = ctx.pathParam("assetName");
        List<Forecast> forecastList = forecastService.getAssetForecasts(assetName, null);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode(200).send(forecastList.toString());
    }

    void userFollowingBasedAssetForecast(RoutingContext ctx) {
        String assetName = ctx.pathParam("assetName");
        String userToken = ctx.request().getCookie("userToken").getValue();
        String username = null;
        if (userToken != null) {
            username = DecryptionUtil.getUsernameFromUserToken(userToken);
        }
        List<Forecast> forecastList = forecastService.getAssetForecasts(assetName, username);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode(200).send(forecastList.toString());
    }
}
