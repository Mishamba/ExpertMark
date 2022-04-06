package com.expert.mark.service.impl;

import com.expert.mark.model.method.MethodData;
import com.expert.mark.model.method.data.TwoMarksData;
import com.expert.mark.service.BasicExpertForecastCalculationService;

public class TwoMarksForecastService implements BasicExpertForecastCalculationService {
    public void process(MethodData methodData) {
        TwoMarksData twoMarksData = (TwoMarksData) methodData;
        if (twoMarksData.getPessimisticMark() >= twoMarksData.getOptimisticMark()) {
            throw new IllegalArgumentException("Pessimistic mark must be lower than optimistic mark.");
        }
        twoMarksData.setResult((twoMarksData.getPessimisticMark() * 3 + twoMarksData.getOptimisticMark() * 2) / 5);
    }
}
