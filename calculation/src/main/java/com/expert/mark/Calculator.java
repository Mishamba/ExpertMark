package com.expert.mark;

import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.BasicExpertForecastCalculationService;
import com.expert.mark.service.method.mapping.MethodMappingHandler;

public class Calculator {
    public Forecast calculateForecast(Forecast forecast) {
        BasicExpertForecastCalculationService service = MethodMappingHandler.getForecastCalculationService(forecast.getMethodType());
        service.process(forecast.getMethodData());
        return forecast;
    }
}
