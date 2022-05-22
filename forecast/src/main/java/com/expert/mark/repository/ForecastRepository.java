package com.expert.mark.repository;

import com.expert.mark.model.content.forecast.Forecast;

import java.util.List;

public interface ForecastRepository {
    Forecast findForecastById(String id);
    List<Forecast> findUsersForecasts(String username);
    List<Forecast> findUsersForecastsForAsset(String username, String asset);
    List<Forecast> findUsersForecastsForAssetFromCurrentDate(String username, String asset);
    boolean deleteForecast(String id);
    Forecast createForecast(Forecast forecast);
    boolean updateForecast(Forecast forecast);
}
