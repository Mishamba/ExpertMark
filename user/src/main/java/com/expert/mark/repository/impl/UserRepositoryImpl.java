package com.expert.mark.repository.impl;

import com.expert.mark.model.account.User;
import com.expert.mark.repository.UserRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserRepositoryImpl implements UserRepository {

    private final String documentName = "user";
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public User save(User user) {
        JsonObject userJson = new JsonObject();
        userJson.put("_id", user.getUsername());
        userJson.remove("username");
        AtomicReference<User> createdUser = new AtomicReference<>();
        mongoClient.save(documentName, userJson).onComplete(res -> {
            if (res.succeeded()) {
                String result = res.result();
                if (result != null) {
                    createdUser.set(new User(new JsonObject(result)));
                }
            } else {
                res.cause().printStackTrace();
            }
        });

        return createdUser.get();
    }

    @Override
    public User update(User user) {
        JsonObject userJson = new JsonObject();
        userJson.put("_id", user.getUsername());
        userJson.remove("username");
        AtomicReference<User> updatedUser = new AtomicReference<>();
        JsonObject query = new JsonObject();
        query.put("_id", user.getUsername());
        mongoClient.findOneAndUpdate(documentName, query, userJson).onComplete(res -> {
            if (res.succeeded()) {
                JsonObject result = res.result();
                if (result != null) {
                    updatedUser.set(new User(result));
                }
            } else {
                res.cause().printStackTrace();
            }
        });

        return updatedUser.get();
    }

    @Override
    public User getUserByUsername(String username) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject fields = new JsonObject();
        fields.put("_id", 1);
        fields.put("createDate", 1);
        fields.put("role", 1);
        fields.put("profile", 0);
        fields.put("character", 1);
        fields.put("isExpert", 1);
        AtomicReference<User> user = new AtomicReference<>();
        mongoClient.findOne(documentName, query, fields).onComplete(res -> {
            if (res.succeeded()) {
                user.set(new User(res.result()));
            } else {
                res.cause().printStackTrace();
            }
        });

        return user.get();
    }

    @Override
    public User getUserByUsernameWithProfile(String username) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        AtomicReference<User> user = new AtomicReference<>();
        mongoClient.findOne(documentName, query, null).onComplete(res -> {
            if (res.succeeded()) {
                user.set(new User(res.result()));
            } else {
                res.cause().printStackTrace();
            }
        });

        return user.get();
    }

    @Override
    public List<String> getUserFollowings(String username) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject fields = new JsonObject();
        fields.put("profile.followingUserNames", 1);
        Future future = mongoClient.findOne(documentName, query, fields).onComplete(res -> {
            if (!res.succeeded()) {
                res.cause().printStackTrace();
            }
        });

        JsonArray result = (JsonArray) future.result();
        List<String> followings = new LinkedList<>();
        for (int i = 0; i < result.size(); i++) {
            followings.add((String) result.getValue(i));
        }

        return followings;
    }

    @Override
    public List<String> getMostTrustedExpertsUsernames() {
        return null;
    }

    @Override
    public boolean addFollowing(String username, String followingUsername) {
        return false;
    }

    @Override
    public boolean removeFollowing(String username, String followingUsername) {
        return false;
    }
}
