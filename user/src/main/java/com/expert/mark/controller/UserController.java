package com.expert.mark.controller;

import com.expert.mark.model.account.User;
import com.expert.mark.service.UserService;
import com.expert.mark.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;

public class UserController extends AbstractVerticle {

    private final UserService userService = new UserServiceImpl();

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/users/profile/:username").handler(this::getUserWithoutProfile);
        router.get("/users/:username").handler(this::getUserWithProfile);
        router.get("/followings").handler(this::getUserFollowings);
        router.get("/most_trusted_experts").handler(this::getMostTrustedExperts);
        router.post("/user").handler(this::createUser);
        router.put("/users/update").handler(this::updateUser);
        router.put("/users/follow/:userToFollow").handler(this::followUser);
        router.put("/users/unfollow/:userToUnfollow").handler(this::unFollowUser);

        vertx.createHttpServer().requestHandler(router).listen(8082);

        vertx.setPeriodic(86400, this::calculateAccuracy);
    }

    private void calculateAccuracy(Long aLong) {

    }


    void getUserWithoutProfile(RoutingContext ctx) {
        String username = ctx.pathParam("username");
        User user = userService.getUserByUsernameWithoutProfile(username);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(user.parseToJson().remove("profile").toString());
    }

    void getUserWithProfile(RoutingContext ctx) {
        String username = ctx.pathParam("username");
        User user = userService.getUserByUsernameWithProfile(username);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send((user == null) ? "no user found" : user.parseToJson().toString());
    }

    void createUser(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        User userToCreate = new User(body);
        User createdUser = userService.createUser(userToCreate);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(createdUser.toString());
    }

    void updateUser(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        User userToUpdate = new User(body);
        User updatedUser = userService.createUser(userToUpdate);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(updatedUser.toString());
    }

    void followUser(RoutingContext ctx) {
        String mainUser = ctx.getBodyAsString();
        String userToFollow = ctx.pathParam("userToFollow");
        boolean isUserFollowed = userService.addFollowing(mainUser, userToFollow);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(isUserFollowed ? 200 : 500).
                send(new JsonObject().put("isUserFollowed", isUserFollowed).toString());
    }

    void unFollowUser(RoutingContext ctx) {
        //TODO
        String mainUser = ctx.getBodyAsString();
        String userToFollow = ctx.pathParam("userToFollow");
        boolean isUserUnfollowed = userService.removeFollowing(mainUser, userToFollow);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(isUserUnfollowed ? 200 : 500).
                send(new JsonObject().put("isUserUnfollowed", isUserUnfollowed).toString());
    }

    void getUserFollowings(RoutingContext ctx) {
        //TODO
        String username = ctx.getBodyAsString();
        List<String> usernames = userService.getUserFollowings(username);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(usernames.toString());
    }

    void getMostTrustedExperts(RoutingContext ctx) {
        List<String> usernames = userService.getMostTrustedExpertsUsernames();
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(usernames.toString());
    }
}
