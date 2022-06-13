package com.expert.mark.service.impl;

import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.repository.impl.ExpertStatisticRepositoryImpl;
import com.expert.mark.repository.impl.ForecastRepositoryImpl;
import com.expert.mark.service.ForecastService;
import com.expert.mark.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository = new ForecastRepositoryImpl();
    private final UserService userService = new UserServiceImpl();
    private final ExpertStatisticRepository expertStatisticRepository = new ExpertStatisticRepositoryImpl();

    @Override
    public Forecast createForecast(Forecast forecast) {
        forecast.setCreateDate(new Date());
        return forecastRepository.createForecast(forecast);
    }



    @Override
    public boolean deleteForecast(String _id) {
        return forecastRepository.deleteForecast(_id);
    }

    @Override
    public boolean updateForecast(Forecast forecast) {
        return forecastRepository.updateForecast(forecast);
    }

    @Override
    public Forecast getForecastById(String id) {
        return forecastRepository.findForecastById(id);
    }

    @Override
    public List<Forecast> getUserForecasts(String username) {
        return forecastRepository.findUsersForecasts(username);
    }

    @Override
    public List<Forecast> getAssetForecasts(String assetName, String username) {
        List<String> experts = userService.getUserFollowings(username);
        return getAssetForecastsFromExperts(assetName, experts);
    }

    @Override
    public ExpertStatistic getExpertStatisticByExpertUsername(String username) {
        return expertStatisticRepository.getExpertStatisticByExpertUsername(username);
    }

    private List<Forecast> getAssetForecastsFromExperts(String asset, List<String> expertsUsernames) {
        List<Forecast> forecasts = new LinkedList<>();
        expertsUsernames.forEach(username -> forecasts.addAll(forecastRepository.findUsersForecastsForAsset(username, asset)));
        return forecasts;
    }
}
