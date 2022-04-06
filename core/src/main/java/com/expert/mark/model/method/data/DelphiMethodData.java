package com.expert.mark.model.method.data;

import com.expert.mark.model.User;
import com.expert.mark.model.method.MethodData;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class DelphiMethodData extends MethodData {
    @NotNull
    private List<DelphiMark> delphiMarkList;
    private Float firstTourQuartileRation;
    @NotNull
    private Integer tourNumber;
    private Boolean anotherTourRequired;
    private Number medianMark;

    public DelphiMethodData(JsonObject jsonObject) {
        delphiMarkList = new ArrayList<>();
        JsonArray jsonDelphiMarkList = jsonObject.getJsonArray("delphiMarkList");
        IntStream.range(0, jsonDelphiMarkList.size()).forEach(x -> delphiMarkList.add(new DelphiMark(jsonDelphiMarkList.getJsonObject(x))));
        this.firstTourQuartileRation = jsonObject.getFloat("firstTourQuartileRation");
        this.tourNumber = jsonObject.getInteger("tourNumber");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class DelphiMark {
        @NotNull
        private String userId;
        @NotNull
        private int mark;

        public DelphiMark(JsonObject jsonObject) {
            this.userId = jsonObject.getString("userId");
            this.mark = jsonObject.getInteger("mark");
        }
    }
}
