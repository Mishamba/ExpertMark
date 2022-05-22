package com.expert.mark.model.parser;

import com.expert.mark.model.content.forecast.method.MethodData;
import com.expert.mark.model.content.forecast.method.data.ThreeMarksData;
import com.expert.mark.model.content.forecast.method.data.ThreeMarksWithChancesData;
import com.expert.mark.model.content.forecast.method.data.TwoMarksData;
import com.expert.mark.model.content.forecast.method.type.MethodType;
import io.vertx.core.json.JsonObject;

public class MethodDataParser {
    public static MethodData parseJsonToMethodData(JsonObject jsonObject, MethodType type) {
        switch (type) {
            case THREE_MARKS_FORECAST:
                return new ThreeMarksData(jsonObject);
            case THREE_MARKS_WITH_CHANCES_FORECAST:
                return new ThreeMarksWithChancesData(jsonObject);
            case TWO_MARKS_FORECAST:
                return new TwoMarksData(jsonObject);
            default:
                throw new RuntimeException("no such method");
        }
    }
    
    public static String parseMethodDataToJson(MethodData data, MethodType type) {
        switch (type) {
            case THREE_MARKS_FORECAST:
                ThreeMarksData threeMarksData = (ThreeMarksData) data;
                return threeMarksData.toString();
            case THREE_MARKS_WITH_CHANCES_FORECAST:
                ThreeMarksWithChancesData threeMarksWithChancesData = (ThreeMarksWithChancesData) data;
                return threeMarksWithChancesData.toString();
            case TWO_MARKS_FORECAST:
                 TwoMarksData twoMarksData = (TwoMarksData) data;
                 return twoMarksData.toString();
            default:
                throw new RuntimeException("no such method");
        }
    }
}
