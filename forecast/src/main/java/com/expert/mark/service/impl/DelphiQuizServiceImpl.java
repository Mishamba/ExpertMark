package com.expert.mark.service.impl;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.QuizStep;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;
import com.expert.mark.repository.DelphiQuizRepository;
import com.expert.mark.repository.impl.DelphiQuizRepositoryImpl;
import com.expert.mark.service.DelphiQuizService;

public class DelphiQuizServiceImpl implements DelphiQuizService {

    private final DelphiQuizRepository delphiQuizRepository = new DelphiQuizRepositoryImpl();

    @Override
    public DelphiQuiz createDelphiQuiz(DelphiQuiz delphiQuiz) {
        delphiQuiz.setQuizSteps(null);
        delphiQuiz.setFirstTourQuartileRation(null);
        if (delphiQuiz.getTitle() == null || delphiQuiz.getAssetName() == null || delphiQuiz.getExpertsUsernames() == null) {
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
    public void startNewQuizStep(String delphiQuizId) {
        DelphiQuiz delphiQuiz = delphiQuizRepository.getDelphiQuizById(delphiQuizId);
        delphiQuiz.getQuizSteps().add(new QuizStep());
        delphiQuizRepository.updateDelphiQuiz(delphiQuiz);
    }
}
