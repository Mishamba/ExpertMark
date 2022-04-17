package com.expert.mark.model.account;

import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Post {
    private String id;
    private Date postDate;
    private Date updateDate;
    private String ownerUsername;
    private String body;

    public Post(JsonObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.postDate = DateParser.parseToDate(jsonObject.getString("postDate"));
        this.updateDate = DateParser.parseToDate(jsonObject.getString("updateDate"));
        this.ownerUsername = jsonObject.getString("ownerUsername");
        this.body = jsonObject.getString("body");
    }
}
