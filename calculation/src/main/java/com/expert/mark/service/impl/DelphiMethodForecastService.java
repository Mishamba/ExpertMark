package com.expert.mark.service.impl;

import com.expert.mark.model.method.MethodData;
import com.expert.mark.model.method.data.DelphiMethodData;
import com.expert.mark.service.BasicExpertForecastCalculationService;
import com.expert.mark.util.comparator.DelphiMarkComparator;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DelphiMethodForecastService implements BasicExpertForecastCalculationService {
    public void process(@NotNull MethodData methodData) {
        DelphiMethodData delphiMethodData = (DelphiMethodData) methodData;

        List<DelphiMethodData.DelphiMark> delphiMarks = delphiMethodData.getDelphiMarkList();
        delphiMarks.sort(new DelphiMarkComparator());
        int medianMark;
        if (delphiMarks.size() % 2 == 0) {
            medianMark = (delphiMarks.get(delphiMarks.size() / 2).getMark() + delphiMarks.get(delphiMarks.size() / 2 + 1).getMark()) / 2;
        } else {
            medianMark = delphiMarks.get((delphiMarks.size() - 1)/2 + 1).getMark();
        }

        delphiMethodData.setMedianMark(medianMark);

        int lowerQuartile = delphiMarks.get(delphiMarks.size() / 4).getMark();
        int higherQuartile = delphiMarks.get(delphiMarks.size() - (delphiMarks.size() / 4) + 1).getMark();

        float currentTourQuartileRation = (float) higherQuartile / (float) lowerQuartile;

        if (delphiMethodData.getFirstTourQuartileRation() != null) {
            delphiMethodData.setFirstTourQuartileRation(currentTourQuartileRation);
            delphiMethodData.setAnotherTourRequired(true);
        } else if (delphiMethodData.getFirstTourQuartileRation() / currentTourQuartileRation <= 1.6) {
            delphiMethodData.setAnotherTourRequired(false);
        }
    }
}
