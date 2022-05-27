package com.expert.mark.model.content.forecast.method.data.delphi.discussion;

import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Message {
    private Date postDate;
    private String message;

    public Message(JsonObject jsonObject) {
        this.postDate = DateParser.parseToDateWithMinutes(jsonObject.getString("postDate"));
        this.message = jsonObject.getString("message");
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("postDate", DateParser.parseToStringWithoutMinutes(this.postDate));
        jsonObject.put("message", this.message);

        return jsonObject;
    }
}
