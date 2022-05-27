package com.expert.mark.service.impl;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.QuizStep;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;
import com.expert.mark.repository.DelphiQuizRepository;
import com.expert.mark.repository.impl.DelphiQuizRepositoryImpl;
import com.expert.mark.service.DelphiQuizService;

import java.util.Date;
import java.util.List;

public class DelphiQuizServiceImpl implements DelphiQuizService {

    private final DelphiQuizRepository delphiQuizRepository = new DelphiQuizRepositoryImpl();

    @Override
    public DelphiQuiz createDelphiQuiz(DelphiQuiz delphiQuiz) {
        delphiQuiz.setQuizSteps(null);
        delphiQuiz.setFirstTourQuartileRation(null);
        delphiQuiz.setDiscussionEndDate(new Date(new Date().getTime() +
                86400L * delphiQuiz.getDiscussionTimeInDays()));
        if (delphiQuiz.getTitle() == null || delphiQuiz.getAssetName() == null ||
                delphiQuiz.getExpertsUsernames() == null) {
            throw new IllegalArgumentException();
        }

        return delphiQuizRepository.createDelphiQuiz(delphiQuiz);
    }

    @Override
    public Message postMessage(Message message, String delphiQuizId) {
        delphiQuizRepository.postDiscussionMessage(message, delphiQuizId);
        return message;
    }

    @Override
    public DelphiQuiz getDelphiQuiz(String delphiQuizId) {
        return delphiQuizRepository.getDelphiQuizById(delphiQuizId);
    }

    @Override
    public SingleMark postSingleMark(String delphiQuizId, SingleMark singleMark) {
        this.getDelphiQuiz(delphiQuizId).getLastStep().getMarks().forEach(mark -> {
            if (mark.getOwnerUsername().equals(singleMark.getOwnerUsername())) {
                throw new RuntimeException();
            }
        });
        return delphiQuizRepository.postSingleMark(delphiQuizId, singleMark);
    }

    @Override
    public void processDelphiQuizzes() {
        List<DelphiQuiz> delphiQuizzes = delphiQuizRepository.getDelphiQuizzesByDiscussionEndDate(new Date());
        delphiQuizzes.forEach(delphiQuiz -> processDelphiQuiz(delphiQuiz.getTitle()));
    }

    @Override
    public void processDelphiQuiz(String delphiQuizId) {
        DelphiQuiz delphiQuiz = delphiQuizRepository.getDelphiQuizById(delphiQuizId);
        List<SingleMark> singleMarkList = delphiQuiz.getLastStep().getMarks();
        singleMarkList.sort(SingleMark::compareTo);
        double medianMark;
        if (singleMarkList.size() % 2 == 0) {
            medianMark = singleMarkList.get(singleMarkList.size() / 2).getMark().getResult() +
                    singleMarkList.get(singleMarkList.size() / 2 + 1).getMark().getResult();
        } else {
            medianMark = singleMarkList.get((singleMarkList.size() - 1) / 2 + 1).getMark().getResult();
        }

        delphiQuiz.getLastStep().setMedianMark(medianMark);

        double lowerQuartile = singleMarkList.get(singleMarkList.size() / 4).getMark().getResult();
        double higherQuartile = singleMarkList.get(singleMarkList.size() -
                (singleMarkList.size() / 4) + 1).getMark().getResult();

        double currentTourQuartileRation = higherQuartile / lowerQuartile;

        if (delphiQuiz.getFirstTourQuartileRation() != null) {
            delphiQuiz.setFirstTourQuartileRation(currentTourQuartileRation);
            delphiQuiz.getLastStep().setAnotherTourRequired(true);
        } else if (delphiQuiz.getFirstTourQuartileRation() / currentTourQuartileRation <= 1.6) {
            delphiQuiz.getLastStep().setAnotherTourRequired(false);
        }

        delphiQuiz.getQuizSteps().add(new QuizStep());

        delphiQuizRepository.updateDelphiQuiz(delphiQuiz);
    }
}
