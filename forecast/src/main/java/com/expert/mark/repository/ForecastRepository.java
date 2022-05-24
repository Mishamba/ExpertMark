package com.expert.mark.repository;

import com.expert.mark.model.content.forecast.Forecast;

import java.util.Date;
import java.util.List;

public interface ForecastRepository {
    Forecast findForecastById(String id);
    List<Forecast> findUsersForecasts(String username);
    List<Forecast> findUsersForecastsForAsset(String username, String asset);
    List<Forecast> findUsersForecastsForAssetFromDate(String username, String asset, Date date);
    List<Forecast> findUsersForecastsFromDate(String username, Date date);
    List<Forecast> findForecastsByTargetDate(Date date);
    List<Forecast> findForecastsForAssetWhereOwnerNotIn(String assetName, List<String> experts);
    List<Forecast> findForecastsForAsset(String assetName);
    boolean deleteForecast(String id);
    Forecast createForecast(Forecast forecast);
    boolean updateForecast(Forecast forecast);
}
