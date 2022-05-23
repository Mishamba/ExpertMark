package com.expert.mark.repository;

import com.expert.mark.model.account.expert.ExpertStatistic;

public interface ExpertStatisticRepository {
    ExpertStatistic getExpertStatisticByExpertUsername(String expertUsername);
    ExpertStatistic saveExpertStatistic(ExpertStatistic expertStatistic);
    ExpertStatistic updateExpertStatistic(ExpertStatistic expertStatistic);
    ExpertStatistic deleteExpertStatisticByExpertUsername(String expertUsername);
}
