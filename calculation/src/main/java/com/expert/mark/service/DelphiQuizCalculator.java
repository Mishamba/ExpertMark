package com.expert.mark.service;

import com.expert.mark.model.forecast.method.MethodData;
import com.expert.mark.model.forecast.method.data.delphi.DelphiQuiz;
import com.expert.mark.model.forecast.method.data.delphi.SingleMark;

import java.util.List;

public class DelphiQuizCalculator {
    public DelphiQuiz process(MethodData methodData) {
        DelphiQuiz quiz = (DelphiQuiz) methodData;
        List<SingleMark> singleMarkList = quiz.getLastStep().getMarks();
        singleMarkList.sort(SingleMark::compareTo);
        double medianMark = 0;
        if (singleMarkList.size() % 2 == 0) {
            medianMark = (singleMarkList.get(singleMarkList.size() / 2).getMark() +
                    singleMarkList.get(singleMarkList.size() / 2 + 1).getMark());
        } else {
            medianMark = singleMarkList.get((singleMarkList.size() - 1) / 2 + 1).getMark();
        }

        quiz.getLastStep().setMedianMark(medianMark);

        double lowerQuartile = singleMarkList.get(singleMarkList.size() / 4).getMark();
        double higherQuartile = singleMarkList.get(singleMarkList.size() - (singleMarkList.size() / 4) + 1).getMark();

        double currentTourQuartileRation = higherQuartile / lowerQuartile;

        if (quiz.getFirstTourQuartileRation() != null) {
            quiz.setFirstTourQuartileRation(currentTourQuartileRation);
            quiz.getLastStep().setAnotherTourRequired(true);
        } else if (quiz.getFirstTourQuartileRation() / currentTourQuartileRation <= 1.6) {
            quiz.getLastStep().setAnotherTourRequired(false);
        }

        return quiz;
    }
}
