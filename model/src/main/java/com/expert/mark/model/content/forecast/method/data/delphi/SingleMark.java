package com.expert.mark.model.content.forecast.method.data.delphi;

import io.vertx.core.json.JsonObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SingleMark implements Comparable<SingleMark> {
    private Double mark;
    private String ownerUsername;

    public SingleMark(JsonObject jsonObject) {
        this.mark = jsonObject.getDouble("mark");
        this.ownerUsername = jsonObject.getString("ownerUsername");
    }

    @Override
    public int compareTo(SingleMark o) {
        return this.mark.compareTo(o.getMark());
    }
}
