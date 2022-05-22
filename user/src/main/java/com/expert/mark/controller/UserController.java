package com.expert.mark.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class UserController extends AbstractVerticle {
    @Override
    public void start() {
        Router getRouter = Router.router(vertx);
        getRouter.get("/users/profile/:username").handler(this::getUserProfile);
        getRouter.get("/users/:username").handler(this::getUser);
        getRouter.get("/followings").handler(this::getUserFollowings);
        getRouter.get("/most_trusted_experts").handler(this::getMostTrustedExperts);
        Router postPutRouter = Router.router(vertx);
        postPutRouter.route().handler(BodyHandler.create());
        postPutRouter.post("/user").handler(this::createUser);
        postPutRouter.put("/users/update").handler(this::updateUser);
        postPutRouter.put("/users/follow/:userToFollow").handler(this::followUser);
        postPutRouter.put("/users/unfollow/:userToUnfollow").handler(this::unFollowUser);

        vertx.createHttpServer().requestHandler(getRouter).requestHandler(postPutRouter).listen(8086);
    }

    void getUserProfile(RoutingContext ctx) {
        String username = ctx.pathParam("username");

    }

    void getUser(RoutingContext ctx) {

    }

    void createUser(RoutingContext ctx) {

    }

    void updateUser(RoutingContext ctx) {

    }

    void followUser(RoutingContext ctx) {

    }

    void unFollowUser(RoutingContext ctx) {

    }

    void getUserFollowings(RoutingContext ctx) {

    }

    void getMostTrustedExperts(RoutingContext ctx) {

    }
}
