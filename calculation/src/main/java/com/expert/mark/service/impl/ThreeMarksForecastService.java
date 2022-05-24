package com.expert.mark.service.impl;

import com.expert.mark.model.content.forecast.method.MethodData;
import com.expert.mark.model.content.forecast.method.data.ThreeMarksData;
import com.expert.mark.service.BasicExpertForecastCalculationService;

public class ThreeMarksForecastService implements BasicExpertForecastCalculationService {
    public void process(MethodData methodData) {
        ThreeMarksData threeMarksData = (ThreeMarksData) methodData;

        if (threeMarksData.getPessimisticMark() >= threeMarksData.getRealisticMark() ||
                threeMarksData.getRealisticMark() >= threeMarksData.getOptimisticMark()) {
            throw new IllegalArgumentException("Pessimistic mark must be lower than realistic mark. And realistic mark can't be bigger than optimistic mark.");
        }

        threeMarksData.setResult((float) ((threeMarksData.getPessimisticMark() + 2 * threeMarksData.getRealisticMark() + threeMarksData.getOptimisticMark()) / 4));
    }
}
