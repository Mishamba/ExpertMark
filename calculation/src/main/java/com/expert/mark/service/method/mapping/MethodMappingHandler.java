package com.expert.mark.service.method.mapping;

import com.expert.mark.model.content.forecast.method.type.MethodType;
import com.expert.mark.service.BasicExpertForecastCalculationService;
import com.expert.mark.service.impl.ThreeMarksForecastService;
import com.expert.mark.service.impl.ThreeMarksWithChancesForecastService;
import com.expert.mark.service.impl.TwoMarksForecastService;

import java.util.HashMap;
import java.util.Map;

public class MethodMappingHandler {
    private static final Map<MethodType, BasicExpertForecastCalculationService> mapping = new HashMap<>();

    static {
        mapping.put(MethodType.THREE_MARKS_FORECAST, new ThreeMarksForecastService());
        mapping.put(MethodType.THREE_MARKS_WITH_CHANCES_FORECAST, new ThreeMarksWithChancesForecastService());
        mapping.put(MethodType.TWO_MARKS_FORECAST, new TwoMarksForecastService());
    }

    public static BasicExpertForecastCalculationService getForecastCalculationService(MethodType methodType) {
        return mapping.get(methodType);
    }
}
