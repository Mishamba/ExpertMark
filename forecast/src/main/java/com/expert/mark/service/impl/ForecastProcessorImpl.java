package com.expert.mark.service.impl;

import com.expert.mark.Calculator;
import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.repository.impl.ExpertStatisticRepositoryImpl;
import com.expert.mark.repository.impl.ForecastRepositoryImpl;
import com.expert.mark.service.ForecastProcessor;

import java.util.Date;
import java.util.List;

public class ForecastProcessorImpl implements ForecastProcessor {
    private final ForecastRepository forecastRepository = new ForecastRepositoryImpl();
    private final ExpertStatisticRepository expertStatisticRepository = new ExpertStatisticRepositoryImpl();
    private final Calculator calculator = new Calculator();

    @Override
    public void processForecastsForUser(String userName) {
        List<Forecast> forecasts = forecastRepository.findUsersForecastsFromDate(userName, new Date());
        for (Forecast forecast : forecasts) {
            String assetName = forecast.getAssetName();
            //TODO get real actualPrice
            Float actualPrice = 50.0F;
            calculator.calculateForecast(forecast, actualPrice);
        }
    }

    @Override
    public void updateExpertStatisticsAndCalculateForecastAccuracy() {
        for (ExpertStatistic expertStatistic : expertStatisticRepository.getUpdateRequiredExpertStatistics()) {
            String username = expertStatistic.getExpertUsername();
            processForecastsForUser(username);
            List<Forecast> forecasts = forecastRepository.findUsersForecasts(username);
            Float expertAccuracy = 0F;
            int quantity = 0;
            for (Forecast forecast : forecasts) {
                quantity++;
                expertAccuracy+=forecast.getAccuracy();
            }
            expertAccuracy/=quantity;
            expertStatistic.setAccuracy(expertAccuracy);
            expertStatisticRepository.updateExpertStatistic(expertStatistic);
        }
    }
}
