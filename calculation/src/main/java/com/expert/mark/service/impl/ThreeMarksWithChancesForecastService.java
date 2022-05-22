package com.expert.mark.service.impl;

import com.expert.mark.model.content.forecast.method.MethodData;
import com.expert.mark.model.content.forecast.method.data.ThreeMarksWithChancesData;
import com.expert.mark.service.BasicExpertForecastCalculationService;

public class ThreeMarksWithChancesForecastService implements BasicExpertForecastCalculationService {
    public void process(MethodData methodData) {
        ThreeMarksWithChancesData threeMarksWithChancesData = (ThreeMarksWithChancesData) methodData;
        if (threeMarksWithChancesData.getPessimisticMark() >= threeMarksWithChancesData.getRealisticMark() ||
                threeMarksWithChancesData.getRealisticMark() >= threeMarksWithChancesData.getOptimisticMark()) {
            throw new IllegalArgumentException("Pessimistic mark must be lower than realistic mark. And realistic mark can't be bigger than optimistic mark.");
        }

        if (threeMarksWithChancesData.getPessimisticChance() + threeMarksWithChancesData.getRealisticChance() +
                threeMarksWithChancesData.getOptimisticChance() == 1) {
            throw new IllegalArgumentException("Chances sum must be 1.");
        }

        threeMarksWithChancesData.setResult(
                threeMarksWithChancesData.getPessimisticMark() * threeMarksWithChancesData.getPessimisticChance() +
                        threeMarksWithChancesData.getRealisticMark() * threeMarksWithChancesData.getRealisticChance() +
                        threeMarksWithChancesData.getOptimisticMark() * threeMarksWithChancesData.getOptimisticChance());
    }
}
