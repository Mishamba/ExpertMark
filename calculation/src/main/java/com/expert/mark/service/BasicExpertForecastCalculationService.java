package com.expert.mark.service;

import com.expert.mark.model.content.forecast.method.MethodData;

public interface BasicExpertForecastCalculationService {
    void process(MethodData methodData);
}