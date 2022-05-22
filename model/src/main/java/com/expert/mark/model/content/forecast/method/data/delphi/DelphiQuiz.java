package com.expert.mark.model.content.forecast.method.data.delphi;

import com.expert.mark.model.content.forecast.method.MethodData;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class DelphiQuiz extends MethodData {
    private List<QuizStep> quizSteps;
    private String title;
    private String description;
    private List<String> expertsUsernames;
    private Double firstTourQuartileRation;

    public QuizStep getLastStep() {
        return this.quizSteps.get(this.quizSteps.size() - 1);
    }

    public DelphiQuiz(JsonObject jsonObject) {
        this.quizSteps = new LinkedList<>();
        jsonObject.getJsonArray("quizSteps").forEach(quizStepObject -> {
            this.quizSteps.add(new QuizStep((JsonObject) quizStepObject));
        });
        this.title = jsonObject.getString("title");
        this.description = jsonObject.getString("description");
        this.expertsUsernames = new LinkedList<>();
        jsonObject.getJsonArray("expertsUsernames").forEach(expertUsernameObject -> {
            this.expertsUsernames.add((String) expertUsernameObject);
        });
        this.firstTourQuartileRation = jsonObject.getDouble("firstTourQuartileRation");
    }

    @Override
    public JsonObject parseToJson() {
        return null;
    }
}
