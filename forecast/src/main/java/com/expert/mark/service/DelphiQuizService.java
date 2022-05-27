package com.expert.mark.service;

import com.expert.mark.model.content.forecast.method.data.delphi.data.DelphiQuiz;
import com.expert.mark.model.content.forecast.method.data.delphi.data.SingleMark;
import com.expert.mark.model.content.forecast.method.data.delphi.discussion.Message;

public interface DelphiQuizService {
    DelphiQuiz createDelphiQuiz(DelphiQuiz delphiQuiz);
    Message postMessage(Message message, String delphiQuizId);
    DelphiQuiz getDelphiQuiz(String delphiQuizId);
    SingleMark postSingleMark(String delphiQuizId, SingleMark singleMark);
    void processDelphiQuiz(String delphiQuizId);
    void processDelphiQuizzes();
}
