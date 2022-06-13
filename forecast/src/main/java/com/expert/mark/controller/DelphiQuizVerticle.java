package com.expert.mark.controller;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;
import com.expert.mark.service.DelphiQuizService;
import com.expert.mark.service.impl.DelphiQuizServiceImpl;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.impl.CookieImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.concurrent.atomic.AtomicReference;

public class DelphiQuizVerticle extends AbstractVerticle {

    private final DelphiQuizService delphiQuizService = new DelphiQuizServiceImpl();
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.put("/delphiQuiz/create").handler(ctx -> {
            if (ctx.request().getHeader("Authorization") != null) {
                vertx.eventBus()
                        .request("authenticate", ctx.request().getHeader("Authorization"))
                        .onComplete(res -> {
                            JsonObject response = (JsonObject) res.result().body();
                            if (response.getString("role").equals("ADMIN")) {
                                this.createDelphiQuiz(ctx);
                            }
                        });
            } else {
                ctx.response().setStatusCode(403).send();
            }
        });
        router.post("/delphiQuiz/:delphiQuizTitle/post/message").handler(ctx -> {
            if (ctx.request().getHeader("Authorization") != null) {
                vertx.eventBus()
                    .request("authenticate", ctx.request().getHeader("Authorization"))
                    .onComplete(res -> {
                        JsonObject response = (JsonObject) res.result().body();
                        if (response.getString("role").equals("USER")) {
                            this.postExpertMessage(ctx);
                        }
                    });
        }});
        router.get("/delphiQuiz/:delphiQuizTitle").handler(this::getDelphiQuiz);
        router.put("/delphiQuiz/forceNewQuizStep").handler(ctx -> {
            vertx.eventBus()
                    .request("authenticate", ctx.request().getHeader("Authorization"))
                    .onComplete(res -> {
                        JsonObject response = (JsonObject) res.result().body();
                        if (response.getString("role").equals("ADMIN")) {
                            this.forceNewQuizStep(ctx);
                        }
                    });
        });
        router.post("/delphiQuiz/:delphiQuizTitle/post/mark").handler(ctx -> {
            vertx.eventBus()
                    .request("authenticate", ctx.request().getHeader("Authorization"))
                    .onComplete(res -> {
                        JsonObject response = (JsonObject) res.result().body();
                        if (response.getString("role").equals("USER")) {
                            ctx.request().cookies().add(new CookieImpl("username", response.getString("username")));
                            this.postMark(ctx);
                        } else {
                            ctx.response().setStatusCode(403).send();
                        }
                    });
        });

        vertx.createHttpServer().requestHandler(router).listen(8084);

        vertx.setPeriodic(86400 * 1000, this::processDelphiQuizzes);
    }

    private void processDelphiQuizzesForce(RoutingContext routingContext) {
        this.processDelphiQuizzes(0L);
    }

    private boolean isAuthorized(RoutingContext ctx, String role) {
        AtomicReference<Boolean> isAuthorized = new AtomicReference<>(false);
        if (ctx.request().getHeader("Authorization") != null) {
            vertx.eventBus()
                    .request("authenticate", ctx.request().getHeader("Authorization"))
                    .onComplete(res -> {
                        JsonObject response = (JsonObject) res.result().body();
                        isAuthorized.set(response.getString("role").equals(role));
                    });
        }

        return isAuthorized.get();
    }

    private void processDelphiQuizzes(Long aLong) {
        delphiQuizService.processDelphiQuizzes();
    }

    private void postMark(RoutingContext routingContext) {
        String delphiQuizId = routingContext.pathParam("delphiQuizTitle");
        JsonObject body = routingContext.getBodyAsJson();
        AtomicReference<String> ownerUsername = new AtomicReference<>();
        for (Cookie cookie : routingContext.request().cookies()) {
            if (cookie.getName().equals("username")) {
                ownerUsername.set(cookie.getValue());
                break;
            }
        }
        body.put("ownerUsername", ownerUsername);
        SingleMark singleMark = new SingleMark(body);
        delphiQuizService.postSingleMark(delphiQuizId, singleMark);
        routingContext.response().setStatusCode(200).send();
    }

    private void getDelphiQuiz(RoutingContext routingContext) {
        String delphiQuizId = routingContext.pathParam("delphiQuizTitle");
        JsonObject query = new JsonObject();
        query.put("_id", delphiQuizId);

        mongoClient.findOne("delphiQuiz", query, null).
                onSuccess(yes -> {
                    yes.put("title", yes.getString("_id")).remove("_id");
                    routingContext.response().putHeader("Content-Type", "application/json").
                            send(yes.encode());
                });
    }

    private void forceNewQuizStep(RoutingContext routingContext) {
        delphiQuizService.processDelphiQuizzes();
        routingContext.response().send();
    }

    private void postExpertMessage(RoutingContext routingContext) {
        String delphiQuizId = routingContext.pathParam("delphiQuizTitle");
        JsonObject body = routingContext.getBodyAsJson();
        Message message = new Message(body);
        delphiQuizService.postMessage(message, delphiQuizId);
        routingContext.response().setStatusCode(200).send();
    }

    private void createDelphiQuiz(RoutingContext routingContext) {
        JsonObject body = routingContext.getBodyAsJson();
        DelphiQuiz delphiQuiz = new DelphiQuiz(body);
        delphiQuizService.createDelphiQuiz(delphiQuiz);
        routingContext.response().setStatusCode(200).send();
    }
}
