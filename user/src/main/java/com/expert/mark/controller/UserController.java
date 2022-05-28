package com.expert.mark.controller;

import com.expert.mark.model.account.User;
import com.expert.mark.service.UserService;
import com.expert.mark.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
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
        router.get("/users/profile/:username").handler(this::getUserWithoutProfile);//tested
        router.get("/users/:username").handler(this::getUserWithProfile);//tested
        router.get("/followings").handler(this::getUserFollowings);//tested
        router.get("/most_trusted_experts").handler(this::getMostTrustedExperts);
        router.post("/user").handler(this::createUser);
        router.put("/users/update").handler(this::updateUser);
        router.put("/users/follow/:userToFollow").handler(this::followUser);
        router.put("/users/unfollow/:userToUnfollow").handler(this::unFollowUser);

        vertx.createHttpServer().requestHandler(router).listen(8082);
    }

    void getUserWithoutProfile(RoutingContext ctx) {
        String username = ctx.pathParam("username");
        String actorUsername = ctx.getBodyAsJson().getString("username");
        sendQueryToSpy(actorUsername, username, "username");
        User user = userService.getUserByUsernameWithoutProfile(username);
        JsonObject userJson = user.parseToJson();
        userJson.remove("profile");
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(userJson.encode());
    }

    void getUserWithProfile(RoutingContext ctx) {
        String username = ctx.pathParam("username");
        String actorUsername = ctx.getBodyAsJson().getString("username");
        sendQueryToSpy(actorUsername, username, "username");
        User user = userService.getUserByUsernameWithProfile(username);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send((user == null) ? "no user found" : user.parseToJson().encode());
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
        String mainUser = ctx.getBodyAsJson().getString("username");
        String userToFollow = ctx.pathParam("userToFollow");
        boolean isUserFollowed = userService.addFollowing(mainUser, userToFollow);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(isUserFollowed ? 200 : 500).
                send(new JsonObject().put("isUserFollowed", isUserFollowed).toString());
    }

    void unFollowUser(RoutingContext ctx) {
        String actorUsername = ctx.getBodyAsJson().getString("username");
        String userToFollow = ctx.pathParam("userToFollow");
        boolean isUserUnfollowed = userService.removeFollowing(actorUsername, userToFollow);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(isUserUnfollowed ? 200 : 500).
                send(new JsonObject().put("isUserUnfollowed", isUserUnfollowed).toString());
    }

    void getUserFollowings(RoutingContext ctx) {
        String username = ctx.getBodyAsJson().getString("username");
        List<String> usernames = userService.getUserFollowings(username);
        JsonArray usernamesJson = new JsonArray();
        usernames.forEach(usernamesJson::add);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(usernamesJson.encode());
    }

    void getMostTrustedExperts(RoutingContext ctx) {
        List<String> usernames = userService.getMostTrustedExpertsUsernames();
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(usernames.toString());
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
