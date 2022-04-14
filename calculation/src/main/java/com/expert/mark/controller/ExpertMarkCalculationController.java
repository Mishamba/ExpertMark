package com.expert.mark.controller;

import com.expert.mark.model.forecast.method.MethodData;
import com.expert.mark.model.forecast.method.type.MethodType;
import com.expert.mark.service.BasicExpertForecastCalculationService;
import com.expert.mark.service.method.mapping.MethodMappingHandler;
import com.expert.mark.util.parser.MethodDataParser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ExpertMarkCalculationController extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.put("/v1/calculate/:methodName").handler(this::calculateForecast);

        vertx.createHttpServer().requestHandler(router).listen(8085).onFailure(Throwable::printStackTrace);
    }

    void calculateForecast(RoutingContext ctx) {
        MethodType methodType = MethodType.valueOf(ctx.pathParam("methodName"));
        JsonObject jsonMethodData = ctx.getBodyAsJson();
        MethodData methodData = MethodDataParser.parseJsonToMethodData(jsonMethodData, methodType);
        BasicExpertForecastCalculationService service = MethodMappingHandler.getForecastCalculationService(methodType);
        service.process(methodData);
        ctx.response().putHeader("content-type", "application/json").
                end(MethodDataParser.parseMethodDataToJson(methodData, methodType));
    }
}
