package com.expert.mark.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class UserController extends AbstractVerticle {
    @Override
    public void start() {
        Router getRouter = Router.router(vertx);
        getRouter.get("/users/profile/:username").handler(this::getUserProfile);
        getRouter.get("/users/:username").handler(this::getUser);
        Router postPutRouter = Router.router(vertx);
        postPutRouter.route().handler(BodyHandler.create());
        postPutRouter.post("/user").handler(this::createUser);
        postPutRouter.put("/users/update/:username").handler(this::updateUser);

        vertx.createHttpServer().requestHandler(getRouter).requestHandler(postPutRouter).listen(8086);
    }

    void getUserProfile(RoutingContext ctx) {

    }

    void getUser(RoutingContext ctx) {

    }

    void createUser(RoutingContext ctx) {

    }

    void updateUser(RoutingContext ctx) {

    }
}
