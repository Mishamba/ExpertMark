package com.expert.mark;

import io.vertx.core.AbstractVerticle;

public class Runner extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.deployVerticle("com.expert.mark.verticle.CurrencyVerticle", res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
        vertx.deployVerticle("com.expert.mark.controller.DelphiQuizVerticle", res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
        vertx.deployVerticle("com.expert.mark.controller.ForecastVerticle", res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
        vertx.deployVerticle("com.expert.mark.controller.RecommendationController", res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
        vertx.deployVerticle("com.expert.mark.verticle.SecurityVerticle", res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
        vertx.deployVerticle("com.expert.mark.verticle.SpyVerticle", res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
        vertx.deployVerticle("com.expert.mark.controller.UserController", res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
    }
}
