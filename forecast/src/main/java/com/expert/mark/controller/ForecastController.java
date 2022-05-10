package com.expert.mark.controller;

import com.expert.mark.model.forecast.Forecast;
import com.expert.mark.service.ForecastService;
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

    @Override
    public void start() throws Exception {
        Router getRouter = Router.router(vertx);
        getRouter.route("/forecasts/:id").handler(this::getForecastById);
        getRouter.route("/forecasts/user_following_based/:assetName").handler(this::userFollowingBasedAssetForecast);
        getRouter.route("/forecasts/user_owned/:username").handler(this::getUsersForecasts);
        getRouter.route("/forecasts/assets/:asset_name").handler(this::getAssetForecasts);
        Router postPutUpdateDeleteRouter = Router.router(vertx);
        postPutUpdateDeleteRouter.route().handler(BodyHandler.create());
        postPutUpdateDeleteRouter.post("/forecasts/create").handler(this::createForecast);
        postPutUpdateDeleteRouter.put("/forecasts/update").handler(this::updateForecast);
        postPutUpdateDeleteRouter.delete("/forecasts/delete").handler(this::deleteForecast);

        vertx.createHttpServer().requestHandler(getRouter).requestHandler(postPutUpdateDeleteRouter).listen(8081).onFailure(Throwable::printStackTrace);
    }

    void getForecastById(RoutingContext ctx) {
        String forecastId = ctx.pathParam("id");
        Forecast forecast = forecastService.getForecastById(forecastId);
        ctx.response().putHeader("Content-Type", "application/json").send(forecast.toString());
    }

    void updateForecast(RoutingContext ctx) {
        JsonObject updatedForecast = ctx.getBodyAsJson();
        Forecast forecastToUpdate = new Forecast(updatedForecast);
        boolean updatedSuccessfully = forecastService.updateForecast(forecastToUpdate);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((updatedSuccessfully) ? 200 : 500).send();
    }

    void createForecast(RoutingContext ctx) {
        JsonObject jsonForecast = ctx.getBodyAsJson();
        Forecast forecastToSave = new Forecast(jsonForecast);
        Forecast savedForecast = forecastService.createForecast(forecastToSave);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((savedForecast.getId() != null && !savedForecast.getId().isEmpty()) ? 200 : 500).send();
    }

    void deleteForecast(RoutingContext ctx) {
        JsonObject updatedForecast = ctx.getBodyAsJson();
        Forecast forecastToUpdate = new Forecast(updatedForecast);
        boolean deletedSuccessfully = forecastService.deleteForecast(forecastToUpdate);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((deletedSuccessfully) ? 200 : 500).send();
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
