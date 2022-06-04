package com.expert.mark.controller;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.ForecastService;
import com.expert.mark.service.ForecastProcessor;
import com.expert.mark.service.impl.ForecastProcessorImpl;
import com.expert.mark.service.impl.ForecastServiceImpl;
import com.expert.mark.util.security.decryption.DecryptionUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.impl.CookieImpl;
import io.vertx.core.json.Json;
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

        router.get("/forecasts/user_following_based/:assetName").handler(this::userFollowingBasedAssetForecast);
        router.get("/forecasts/user_owned/:username").handler(this::getUsersForecasts);
        router.get("/forecasts/assets/:assetName").handler(this::getAssetForecasts);
        router.put("/forecasts/create").handler(ctx -> {
            vertx.eventBus()
                    .request("authenticate", ctx.request().getHeader("Authorization"))
                    .onComplete(res -> {
                        JsonObject response = (JsonObject) res.result().body();
                        if (response.getString("role").equals("USER")) {
                            ctx.request().cookies().add(new CookieImpl("username", response.getString("username")));
                            this.createForecast(ctx);
                        } else {
                            ctx.response().setStatusCode(403).send();
                        }
                    });
        });
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

    void createForecast(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String username = null;
        for (Cookie cookie : ctx.request().cookies()) {
            if (cookie.getName().equals("username")) {
                username = cookie.getValue();
                break;
            }
        }
        Forecast forecast = new Forecast(body.getJsonObject("forecast").put("ownerUsername", username));
        if (username != null) {
            this.sendQueryToSpy(username, forecast.getAssetName(), "assetName");
        }
        Forecast savedForecast = forecastService.createForecast(forecast);
        ctx.response().putHeader("Content-Type", "application/json").
                setStatusCode((savedForecast.get_id() != null && !savedForecast.get_id().isEmpty()) ? 200 : 500).
                send(savedForecast.parseToJson().encode());
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
