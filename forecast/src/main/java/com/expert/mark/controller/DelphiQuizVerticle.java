package com.expert.mark.controller;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;
import com.expert.mark.service.DelphiQuizService;
import com.expert.mark.service.impl.DelphiQuizServiceImpl;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class DelphiQuizVerticle extends AbstractVerticle {

    private final DelphiQuizService delphiQuizService = new DelphiQuizServiceImpl();
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.put("/delphiQuiz/create").handler(this::createDelphiQuiz);
        router.post("/delphiQuiz/:delphiQuizTitle/post/message").handler(this::postExpertMessage);
        router.get("/delphiQuiz/:delphiQuizTitle").handler(this::getDelphiQuiz);
        router.put("/delphiQuiz/forceNewQuizStep").handler(this::forceNewQuizStep);
        router.post("/delphiQuiz/:delphiQuizTitle/post/mark").handler(this::postMark);

        vertx.createHttpServer().requestHandler(router).listen(8084);

        vertx.setPeriodic(86400 * 1000, this::processDelphiQuizzes);
    }

    private void processDelphiQuizzes(Long aLong) {
        delphiQuizService.processDelphiQuizzes();
    }

    private void postMark(RoutingContext routingContext) {
        String delphiQuizId = routingContext.pathParam("delphiQuizTitle");
        JsonObject body = routingContext.getBodyAsJson();
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