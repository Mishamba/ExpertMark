package com.expert.mark.model.content.forecast.method.data.delphi;

import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class QuizStep {
    private List<SingleMark> marks;
    private Date justificationFinishDate;
    private Boolean anotherTourRequired;
    private Double medianMark;

    public QuizStep(JsonObject jsonObject) {
        this.marks = new LinkedList<>();
        jsonObject.getJsonArray("marks").forEach(markObject -> {
            this.marks.add(new SingleMark((JsonObject) markObject));
        });
        this.justificationFinishDate = DateParser.parseToDate(jsonObject.getString("justificationFinishDate"));
        this.anotherTourRequired = jsonObject.getBoolean("anotherTourRequired");
        this.medianMark = jsonObject.getDouble("medianMark");
    }
}