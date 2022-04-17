package com.expert.mark.repository.impl;

import com.expert.mark.model.forecast.Forecast;
import com.expert.mark.repository.ForecastRepository;

import java.util.List;

public class ForecastRepositoryImpl implements ForecastRepository {
    @Override
    public Forecast findForecastById(String id) {
        return null;
    }

    @Override
    public List<Forecast> findUsersForecasts(String username) {
        return null;
    }

    @Override
    public List<Forecast> findUsersForecastsForAsset(String username, String asset) {
        return null;
    }

    @Override
    public List<Forecast> findUsersForecastsForAssetFromCurrentDate(String username, String asset) {
        return null;
    }

    @Override
    public boolean deleteForecast(String id) {
        return false;
    }

    @Override
    public Forecast createForecast(Forecast forecast) {
        return null;
    }

    @Override
    public boolean updateForecast(Forecast forecast) {
        return false;
    }
}
