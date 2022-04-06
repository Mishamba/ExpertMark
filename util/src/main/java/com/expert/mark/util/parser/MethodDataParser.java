package com.expert.mark.util.parser;

import com.expert.mark.model.method.MethodData;
import com.expert.mark.model.method.data.DelphiMethodData;
import com.expert.mark.model.method.data.ThreeMarksData;
import com.expert.mark.model.method.data.ThreeMarksWithChancesData;
import com.expert.mark.model.method.data.TwoMarksData;
import com.expert.mark.model.method.type.MethodType;
import io.vertx.core.json.JsonObject;

public class MethodDataParser {
    public static MethodData parseJsonToMethodData(JsonObject jsonObject, MethodType type) {
        switch (type) {
            case DELPHI_METHOD_FORECAST:
                return new DelphiMethodData(jsonObject);
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
            case DELPHI_METHOD_FORECAST:
                DelphiMethodData delphiMethodData = (DelphiMethodData) data;
                return delphiMethodData.toString();
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
