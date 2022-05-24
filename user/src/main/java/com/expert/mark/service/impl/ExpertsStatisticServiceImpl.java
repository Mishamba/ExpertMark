package com.expert.mark.service.impl;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.repository.impl.ExpertStatisticRepositoryImpl;
import com.expert.mark.service.ExpertStatisticService;
import io.vertx.core.json.JsonObject;

public class ExpertsStatisticServiceImpl implements ExpertStatisticService {
    private final ExpertStatisticRepository expertStatisticRepository = new ExpertStatisticRepositoryImpl();

    @Override
    public ExpertStatistic getExpertStatisticByExpertUsername(String expertUsername) {
        return expertStatisticRepository.getExpertStatisticByExpertUsername(expertUsername);
    }

    @Override
    public ExpertStatistic becomeExpert(String expertUsername) {
        return expertStatisticRepository.saveExpertStatistic(new ExpertStatistic(
                new JsonObject().put("expertUsername", expertUsername)));
    }
}
