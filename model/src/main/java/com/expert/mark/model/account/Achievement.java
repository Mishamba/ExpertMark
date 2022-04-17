package com.expert.mark.model.account;

import io.vertx.core.json.JsonObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Achievement {
    private String name;
    private Integer price;

    public Achievement(JsonObject jsonObject) {
        this.name = jsonObject.getString("name");
        this.price = jsonObject.getInteger("price");
    }
}
