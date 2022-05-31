package com.expert.mark.service;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.model.content.forecast.Forecast;

import java.util.List;

public interface ForecastService {
    Forecast createForecast(Forecast forecast);
    boolean deleteForecast(String _id);
    boolean updateForecast(Forecast forecast);
    Forecast getForecastById(String id);
    List<Forecast> getUserForecasts(String username);
    List<Forecast> getAssetForecasts(String assetName, String username);
    ExpertStatistic getExpertStatisticByExpertUsername(String username);
}
