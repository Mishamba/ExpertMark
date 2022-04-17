package com.expert.mark.service;

import com.expert.mark.model.forecast.Forecast;

import java.util.List;

public interface ForecastService {
    Forecast createForecast(Forecast forecast);
    boolean deleteForecast(Forecast forecast);
    boolean updateForecast(Forecast forecast);
    Forecast getForecastById(String id);
    List<Forecast> getUserForecasts(String username);
    List<Forecast> getAssetForecasts(String assetName, String username);
}
