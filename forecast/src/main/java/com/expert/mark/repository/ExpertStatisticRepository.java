package com.expert.mark.repository;

import com.expert.mark.model.account.expert.ExpertStatistic;

import java.util.List;

public interface ExpertStatisticRepository {
    ExpertStatistic getExpertStatisticByExpertUsername(String expertUsername);
    ExpertStatistic updateExpertStatistic(ExpertStatistic expertStatistic);
    List<ExpertStatistic> getUpdateRequiredExpertStatistics();
    void setUpdateRequired(String username);
}
