package com.expert.mark;

import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.BasicExpertForecastCalculationService;
import com.expert.mark.service.method.mapping.MethodMappingHandler;

public class Calculator {
    public void calculateForecast(Forecast forecast) {
        BasicExpertForecastCalculationService service = MethodMappingHandler.getForecastCalculationService(forecast.getMethodType());
        service.process(forecast.getMethodData());
    }

    public void calculateForecast(Forecast forecast, Float currentPrice) {
        calculateForecast(forecast);
        Float predictedPrice = forecast.getMethodData().getResult();

        Float accuracy = 100 - (Math.abs(predictedPrice - currentPrice)/currentPrice * 100);
        forecast.setAccuracy(accuracy);
    }
}
