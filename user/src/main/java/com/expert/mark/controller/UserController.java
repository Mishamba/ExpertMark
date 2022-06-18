package com.expert.mark.controller;

import com.expert.mark.model.account.User;
import com.expert.mark.service.UserService;
import com.expert.mark.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.impl.CookieImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserController extends AbstractVerticle {

    private final UserService userService = new UserServiceImpl();

    @Override
    public void start() {
        Router router = Router.router(vertx);
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("Authorization");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.PUT);
        allowMethods.add(HttpMethod.OPTIONS);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        router.route().handler(BodyHandler.create());
        router.get("/users/profile/:username").handler(ctx -> {
            if (ctx.request().getHeader("Authorization") != null) {
                vertx.eventBus()
                        .request("authenticate", ctx.request().getHeader("Authorization"))
                        .onComplete(res -> {
                            JsonObject body = (JsonObject) res.result().body();
                            ctx.request().cookies().add(new CookieImpl("username", body.getString("username")));
                            this.getUserWithoutProfile(ctx);
                        });
            }
        });
        router.get("/users/:username").handler(ctx -> {
            if (ctx.request().getHeader("Authorization") != null) {
                vertx.eventBus()
                        .request("authenticate", ctx.request().getHeader("Authorization"))
                        .onComplete(res -> {
                            JsonObject body = (JsonObject) res.result().body();
                            ctx.request().cookies().add(new CookieImpl("username", body.getString("username")));
                            this.getUserWithProfile(ctx);
                        });
            }
        });

        router.get("/followings").handler(this::getUserFollowings);
        router.get("/most_trusted_experts").handler(this::getMostTrustedExperts);
        router.post("/user").handler(this::createUser);
        router.put("/users/update").handler(this::updateUser);
        router.put("/users/follow/:userToFollow").handler(this::followUser);
        router.put("/users/unfollow/:userToUnfollow").handler(this::unFollowUser);

        vertx.createHttpServer().requestHandler(router).listen(8082);
    }


        void getUserWithoutProfile(RoutingContext ctx) {
        String username = ctx.pathParam("username");
        String actorUsername = ctx.request().getCookie("username").getValue();
        if (actorUsername != null) {
            sendQueryToSpy(actorUsername, username, "username");
        }
        User user = userService.getUserByUsernameWithoutProfile(username);
        JsonObject userJson = user.parseToJson();
        userJson.remove("profile");
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200).send(userJson.encode());
    }

    void getUserWithProfile(RoutingContext ctx) {
        String username = ctx.pathParam("username");
        String actorUsername = ctx.request().getCookie("username").getValue();
        if (actorUsername != null) {
            if (!actorUsername.equals(username)) {
                sendQueryToSpy(actorUsername, username, "username");
            }
        }
        User user = userService.getUserByUsernameWithProfile(username);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200)
                .send((user == null) ? "no user found" : user.parseToJson().encode());
    }

    void createUser(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        User userToCreate = new User(body);
        userService.createUser(userToCreate);
        ctx.response().putHeader("Content-Type", "application/json").setStatusCode(200)
                .send(new JsonObject().put("username", userToCreate.getUsername()).encode());
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
