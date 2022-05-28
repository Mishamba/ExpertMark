package com.expert.mark.model.content.forecast.method.data.delphi.data;

import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;
import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonArray;
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
@EqualsAndHashCode(callSuper = false)
public class DelphiQuiz {
    private List<QuizStep> quizSteps;
    private String assetName;
    private String title;
    private String description;
    private Integer discussionTimeInDays;
    private List<String> expertsUsernames;
    private Date discussionEndDate;
    private List<Message> discussion;
    private Date createDate;
    private Double firstTourQuartileRation;

    public QuizStep getLastStep() {
        return this.quizSteps.get(this.quizSteps.size() - 1);
    }

    public int getStepsNumbers() {
        return this.quizSteps.size();
    }

    public DelphiQuiz(JsonObject jsonObject) {
        this.quizSteps = new LinkedList<>();
        JsonArray quizSteps = jsonObject.getJsonArray("quizSteps");
        if (quizSteps != null) {
            quizSteps.forEach(quizStepObject -> this.quizSteps.add(new QuizStep((JsonObject) quizStepObject)));
        }
        this.assetName = jsonObject.getString("assetName");
        this.title = jsonObject.getString("title");
        this.description = jsonObject.getString("description");
        this.discussionEndDate = DateParser.parseToDateWithoutMinutes(jsonObject.getString("discussionEndDate"));
        this.discussion = new LinkedList<>();
        JsonArray discussion = jsonObject.getJsonArray("discussion");
        if (discussion != null) {
            discussion.forEach(x -> this.discussion.add(new Message((JsonObject) x)));
        }
        this.discussionTimeInDays = jsonObject.getInteger("discussionTimeInDays");
        this.expertsUsernames = new LinkedList<>();
        JsonArray expertsUsernames = jsonObject.getJsonArray("expertsUsernames");
        if (expertsUsernames != null) {
            jsonObject.getJsonArray("expertsUsernames").forEach(expertUsernameObject -> this.expertsUsernames.add((String) expertUsernameObject));
        }
        this.createDate = DateParser.parseToDateWithoutMinutes(jsonObject.getString("createDate"));
        this.firstTourQuartileRation = jsonObject.getDouble("firstTourQuartileRation");
    }

    public JsonObject parseToJson() {
        JsonObject jsonObject = new JsonObject();

        JsonArray quizStepsArray = new JsonArray();
        if (this.quizSteps != null) {
            this.quizSteps.forEach(quizStep -> quizStepsArray.add(quizStep.parseToJson()));
        }
        JsonArray expertsUsernamesArray = new JsonArray();
        if (this.expertsUsernames != null) {
            this.expertsUsernames.forEach(expertsUsernamesArray::add);
        }
        JsonArray discussionArray = new JsonArray();
        if (this.discussion != null) {
            this.discussion.forEach(disc -> discussionArray.add(disc.parseToJson()));
        }

        jsonObject.put("quizSteps", quizStepsArray);
        jsonObject.put("assetName", this.assetName);
        jsonObject.put("title", this.title);
        jsonObject.put("description", this.description);
        jsonObject.put("discussionTimeInDays", this.discussionTimeInDays);
        jsonObject.put("expertsUsernames", expertsUsernamesArray);
        jsonObject.put("createDate", DateParser.parseToStringWithoutMinutes(this.createDate));
        jsonObject.put("discussionEndDate", DateParser.parseToStringWithoutMinutes(this.discussionEndDate));
        jsonObject.put("discussion", discussionArray);
        jsonObject.put("firstTourQuartileRation", this.firstTourQuartileRation);

        return jsonObject;
    }
}
