package com.expert.mark.repository;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;

public interface DelphiQuizRepository {
    DelphiQuiz getDelphiQuizById(String id);
    DelphiQuiz createDelphiQuiz(DelphiQuiz delphiQuiz);
    DelphiQuiz updateDelphiQuiz(DelphiQuiz delphiQuiz);
    void addDelphiQuizStep(String delphiQuizId);
    Message postDiscussionMessage(Message message, String delphiQuizId);
    int getDelphiQuizStepsNumber(String delphiQuizId);
    SingleMark postSingleMark(String delphiQuizId, SingleMark singleMark);
}
