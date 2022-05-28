package com.expert.mark.service.impl;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.QuizStep;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;
import com.expert.mark.repository.DelphiQuizRepository;
import com.expert.mark.repository.impl.DelphiQuizRepositoryImpl;
import com.expert.mark.service.DelphiQuizService;
import com.expert.mark.util.parser.DateParser;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DelphiQuizServiceImpl implements DelphiQuizService {

    private final DelphiQuizRepository delphiQuizRepository = new DelphiQuizRepositoryImpl();

    @Override
    public DelphiQuiz createDelphiQuiz(DelphiQuiz delphiQuiz) {
        delphiQuiz.setQuizSteps(null);
        delphiQuiz.setFirstTourQuartileRation(null);
        List<QuizStep> quizSteps = new LinkedList<>();
        quizSteps.add(new QuizStep(new JsonObject().put("stepNumber", 1).put("marks", new JsonArray())));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, delphiQuiz.getDiscussionTimeInDays());
        quizSteps.get(0).setJustificationFinishDate(calendar.getTime());
        delphiQuiz.setQuizSteps(quizSteps);
        delphiQuiz.setCreateDate(new Date());
        delphiQuiz.setDiscussionEndDate(calendar.getTime());
        if (delphiQuiz.getTitle() == null || delphiQuiz.getAssetName() == null ||
                delphiQuiz.getExpertsUsernames() == null) {
            throw new IllegalArgumentException();
        }

        return delphiQuizRepository.createDelphiQuiz(delphiQuiz);
    }

    @Override
    public Message postMessage(Message message, String delphiQuizId) {
        message.setPostDate(new Date());
        delphiQuizRepository.postDiscussionMessage(message, delphiQuizId);
        return message;
    }

    @Override
    public DelphiQuiz getDelphiQuiz(String delphiQuizId) {
        DelphiQuiz delphiQuiz = delphiQuizRepository.getDelphiQuizById(delphiQuizId);
        delphiQuiz.setTitle(delphiQuizId);
        return delphiQuiz;
    }

    @Override
    public SingleMark postSingleMark(String delphiQuizId, SingleMark singleMark) {
        DelphiQuiz delphiQuiz = this.getDelphiQuiz(delphiQuizId);

        List<SingleMark> marks =  delphiQuiz.getLastStep().getMarks();
        if (marks != null) {
            marks.forEach(mark -> {
                if (mark.getOwnerUsername().equals(singleMark.getOwnerUsername())) {
                    throw new RuntimeException();
                }
            });
        }

        boolean isExpert = false;
        for (String username : delphiQuiz.getExpertsUsernames()) {
            if (username.equals(singleMark.getOwnerUsername())) {
                isExpert = true;
                break;
            }
        }
        if (!isExpert) {
            throw new RuntimeException("not expert");
        }

        if (!delphiQuiz.getLastStep().getAnotherTourRequired()) {
            throw new RuntimeException("quiz is finished");
        }

        delphiQuiz.getLastStep().getMarks().add(singleMark);
        delphiQuizRepository.createDelphiQuiz(delphiQuiz);

        return singleMark;
    }

    @Override
    public void processDelphiQuizzes() {
        List<DelphiQuiz> delphiQuizzes = delphiQuizRepository.getDelphiQuizzesRequiredToProcess(new Date());
        delphiQuizzes.forEach(delphiQuiz -> {
            if (delphiQuiz.getLastStep().getAnotherTourRequired()) {
                processDelphiQuiz(delphiQuiz.getTitle());
            }
        });
    }

    @Override
    public void processDelphiQuiz(String delphiQuizId) {
        DelphiQuiz delphiQuiz = delphiQuizRepository.getDelphiQuizById(delphiQuizId);
        if (delphiQuiz.getLastStep().getMarks() == null) {
            return;
        }
        List<SingleMark> singleMarkList = delphiQuiz.getLastStep().getMarks();
        singleMarkList.sort(SingleMark::compareTo);
        double medianMark;
        if (singleMarkList.size() % 2 == 0) {
            medianMark = singleMarkList.get((singleMarkList.size() / 2) - 1).getMark().getResult() +
                    singleMarkList.get(singleMarkList.size() / 2).getMark().getResult();
        } else {
            medianMark = singleMarkList.get((singleMarkList.size() - 1) / 2 + 1).getMark().getResult();
        }

        delphiQuiz.getLastStep().setMedianMark(medianMark);

        double lowerQuartile = singleMarkList.get(singleMarkList.size() / 4).getMark().getResult();
        double higherQuartile = singleMarkList.get(singleMarkList.size() -
                (singleMarkList.size() / 4) - 1).getMark().getResult();

        double currentTourQuartileRation = higherQuartile / lowerQuartile;

        if (delphiQuiz.getFirstTourQuartileRation() == null) {
            delphiQuiz.setFirstTourQuartileRation(currentTourQuartileRation);
            delphiQuiz.getLastStep().setAnotherTourRequired(true);
        } else {
            delphiQuiz.getLastStep().setAnotherTourRequired(!(delphiQuiz.getFirstTourQuartileRation() / currentTourQuartileRation <= 1.6));
        }

        if (delphiQuiz.getLastStep().getAnotherTourRequired()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, delphiQuiz.getDiscussionTimeInDays());
            delphiQuiz.getQuizSteps().add(new QuizStep(new JsonObject().put("stepNumber", delphiQuiz.getStepsNumbers() + 1).
                    put("justificationFinishDate", DateParser.parseToStringWithoutMinutes(calendar.getTime()))));
            delphiQuiz.setDiscussionEndDate(calendar.getTime());
        }

        delphiQuizRepository.createDelphiQuiz(delphiQuiz);
    }
}
