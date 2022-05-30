package com.expert.mark.repository.impl;

import com.expert.mark.model.account.User;
import com.expert.mark.repository.UserRepository;
import com.expert.mark.util.db.DatabaseClientProvider;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class UserRepositoryImpl implements UserRepository {

    private final String userDocumentName = "expertUser";
    private final String expertStatisticDocumentName = "expert_statistic";
    private final MongoClient mongoClient = DatabaseClientProvider.provide();

    @Override
    public User save(User user) {
        JsonObject userJson = user.parseToJson();
        userJson.put("_id", user.getUsername());
        userJson.remove("username");
        AtomicReference<User> createdUser = new AtomicReference<>();
        mongoClient.save(userDocumentName, userJson).onComplete(res -> {
            if (res.succeeded()) {
                mongoClient.findOne(userDocumentName, new JsonObject().put("_id", user.getUsername()), null,  findRes -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    createdUser.set(new User(findRes.result()));
                });
            }
        }).onFailure(Throwable::printStackTrace);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        mongoClient.findOneAndUpdate(userDocumentName, query, userJson).onComplete(res -> {
            if (res.succeeded()) {
                JsonObject result = res.result();
                if (result != null) {
                    updatedUser.set(new User(result));
                }
            } else {
                res.cause().printStackTrace();
            }
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return updatedUser.get();
    }

    @Override
    public User getUserByUsernameWithoutProfile(String username) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject fields = new JsonObject();
        fields.put("_id", 1);
        fields.put("profile", 0);
        AtomicReference<User> user = new AtomicReference<>();
        mongoClient.findOne(userDocumentName, query, fields).onComplete(res -> {
            if (res.succeeded()) {
                JsonObject userJson = res.result();
                userJson.put("username", userJson.getString("_id"));
                user.set(new User(userJson));
            } else {
                res.cause().printStackTrace();
            }
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

            return user.get();
    }

    @Override
    public User getUserByUsernameWithProfile(String username) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        AtomicReference<User> user = new AtomicReference<>();
        mongoClient.findOne(userDocumentName, query, null).onComplete(res -> {
            if (res.succeeded()) {
                JsonObject jsonObject = res.result();
                jsonObject.put("username", jsonObject.getString("_id"));
                user.set(new User(jsonObject));
            } else {
                res.cause().printStackTrace();
            }
        }).onFailure(Throwable::printStackTrace);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return user.get();
    }

    @Override
    public List<String> getUserFollowings(String username) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject fields = new JsonObject();
        fields.put("profile.followingUserNames", 1);
        Future<JsonObject> future = mongoClient.findOne(userDocumentName, query, fields).onComplete(res -> {
            if (!res.succeeded()) {
                res.cause().printStackTrace();
            }
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JsonArray result = future.result().getJsonObject("profile").getJsonArray("followingUserNames");
        List<String> followings = new LinkedList<>();
        for (int i = 0; i < result.size(); i++) {
            followings.add((String) result.getValue(i));
        }

        return followings;
    }

    @Override
    public List<String> getMostTrustedExpertsUsernames() {
        FindOptions findOptions = new FindOptions();
        findOptions.setLimit(5);
        findOptions.setSort(new JsonObject().put("accuracy", 1));
        findOptions.setFields(new JsonObject().
                put("expertUsername", true).
                put("character", false).
                put("accuracy", false));

        List<String> usernames = new LinkedList<>();
        mongoClient.findWithOptions(expertStatisticDocumentName, null, findOptions, res -> {
            if (!res.succeeded()) {
                res.cause().printStackTrace();
            } else {
                res.result().forEach(x -> usernames.add(x.getString("expertUsername")));
            }
        }).close();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return usernames;
    }

    @Override
    public boolean addFollowing(String username, String followingUsername) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject update = new JsonObject();
        update.put("$push", new JsonObject().put("profile.followingUserNames", followingUsername));
        AtomicReference<Boolean> succeeded = new AtomicReference<>(false);
        mongoClient.updateCollection(userDocumentName, query, update).onComplete(res -> {
            succeeded.set(res.succeeded());
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return succeeded.get();
    }

    @Override
    public boolean removeFollowing(String username, String followingUsername) {
        JsonObject query = new JsonObject();
        query.put("_id", username);
        JsonObject update = new JsonObject();
        update.put("$pull", new JsonObject().put("profile.followingUserNames", followingUsername));
        AtomicReference<Boolean> succeeded = new AtomicReference<>();
        mongoClient.updateCollection(userDocumentName, query, update).onComplete(res -> {
            succeeded.set(res.succeeded());
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return succeeded.get();
    }
}
