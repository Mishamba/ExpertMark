package com.expert.mark.model.account;

import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonObject;

import java.util.Date;

public class Post {
    private Date postDate;
    private Date updateDate;
    private String ownerUsername;
    private String body;

    public Post(JsonObject jsonObject) {
        this.postDate = DateParser.parseToDate(jsonObject.getString("postDate"));
        this.updateDate = DateParser.parseToDate(jsonObject.getString("updateDate"));
        this.ownerUsername = jsonObject.getString("ownerUsername");
        this.body = jsonObject.getString("body");
    }
}
