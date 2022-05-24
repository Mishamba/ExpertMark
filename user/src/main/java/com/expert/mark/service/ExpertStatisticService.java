package com.expert.mark.service;

import com.expert.mark.model.account.expert.ExpertStatistic;

public interface ExpertStatisticService {
    ExpertStatistic getExpertStatisticByExpertUsername(String expertUsername);
    ExpertStatistic becomeExpert(String expertUsername);
}
