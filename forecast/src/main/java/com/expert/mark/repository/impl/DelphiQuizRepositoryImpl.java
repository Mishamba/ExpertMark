package com.expert.mark.repository.impl;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.QuizStep;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;
import com.expert.mark.repository.DelphiQuizRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.concurrent.atomic.AtomicReference;

public class DelphiQuizRepositoryImpl implements DelphiQuizRepository {

    private final String delphiQuizDocumentName = "delphiQuiz";
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public DelphiQuiz getDelphiQuizById(String id) {
        JsonObject query = new JsonObject();
        query.put("id", id);
        AtomicReference<DelphiQuiz> delphiQuiz = new AtomicReference<>();
        mongoClient.findOne(delphiQuizDocumentName, query, null).onComplete(res -> {
            delphiQuiz.set(new DelphiQuiz(res.result()));
        });

        return delphiQuiz.get();
    }

    @Override
    public DelphiQuiz createDelphiQuiz(DelphiQuiz delphiQuiz) {
        JsonObject delphiQuizJson = delphiQuiz.parseToJson();
        delphiQuizJson.put("_id", delphiQuiz.getTitle());
        delphiQuizJson.remove("title");
        mongoClient.save(delphiQuizDocumentName, delphiQuizJson).onSuccess(res -> {
            if (!delphiQuiz.getTitle().equals(res)) {
                throw new RuntimeException("delphiQuiz wasn't created");
            }
        });

        return delphiQuiz;
    }

    @Override
    public DelphiQuiz updateDelphiQuiz(DelphiQuiz delphiQuiz) {
        JsonObject query = new JsonObject();
        query.put("_id", delphiQuiz.getTitle());
        JsonObject delphiQuizJson = delphiQuiz.parseToJson();
        delphiQuizJson.put("_id", delphiQuiz.getTitle());
        delphiQuizJson.remove("title");
        delphiQuizJson.remove("expertsUsernames");
        AtomicReference<DelphiQuiz> updatedDelphiQuiz = new AtomicReference<>();
        mongoClient.updateCollection(delphiQuizDocumentName, query, delphiQuizJson).
                onSuccess(res -> updatedDelphiQuiz.set(new DelphiQuiz(res.toJson())));

        return updatedDelphiQuiz.get();
    }

    @Override
    public void addDelphiQuizStep(String delphiQuizId) {
        JsonObject query = new JsonObject();
        query.put("_id", delphiQuizId);
        QuizStep quizStep = new QuizStep(new JsonObject());
        JsonObject updateData = new JsonObject();
        updateData.put("$push", new JsonObject().put("quizSteps", quizStep.parseToJson()));

        mongoClient.updateCollection(delphiQuizDocumentName, query, updateData);
    }

    @Override
    public Message postDiscussionMessage(Message message, String delphiQuizId) {
        JsonObject query = new JsonObject();
        query.put("_id", delphiQuizId);
        JsonObject updateData = new JsonObject();
        updateData.put("$push", new JsonObject().put("discussion", message.parseToJson()));

        mongoClient.updateCollection(delphiQuizDocumentName, query, updateData);

        return message;
    }

    @Override
    public int getDelphiQuizStepsNumber(String delphiQuizId) {
        return this.getDelphiQuizById(delphiQuizId).getStepsNumbers();
    }

    @Override
    public SingleMark postSingleMark(String delphiQuizId, SingleMark singleMark) {
        JsonObject query = new JsonObject();
        query.put("_id", delphiQuizId);
        JsonObject updateData = new JsonObject();
        int quizNumber = this.getDelphiQuizById(delphiQuizId).getStepsNumbers();
        updateData.put("$push", new JsonObject().put("quizSteps." + quizNumber + ".singleMarks",
                singleMark.parseToJson()));

        mongoClient.updateCollection(delphiQuizDocumentName, query, updateData);

        return singleMark;
    }
}
