package com.expert.mark.service.impl;

import com.expert.mark.Calculator;
import com.expert.mark.model.forecast.Forecast;
import com.expert.mark.repository.ForecastRepository;
import com.expert.mark.repository.impl.ForecastRepositoryImpl;
import com.expert.mark.service.ForecastService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

import java.util.LinkedList;
import java.util.List;

public class ForecastServiceImpl implements ForecastService {

    private final WebClient client = WebClient.create(Vertx.vertx());
    private final ForecastRepository forecastRepository = new ForecastRepositoryImpl();
    private final Calculator calculator = new Calculator();

    @Override
    public Forecast createForecast(Forecast forecast) {
        calculator.calculateForecast(forecast);
        return forecastRepository.createForecast(forecast);
    }

    @Override
    public boolean deleteForecast(Forecast forecast) {
        return forecastRepository.deleteForecast(forecast.getId());
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
        String callUrl = (username == null || username.isEmpty()) ? "/most_trusted_experts" : "/following/" + username;
        List<String> experts = new LinkedList<>();
        client.get(8080, "localhost", callUrl).send().
                onSuccess(response -> response.bodyAsJsonArray().forEach(expertUsername -> experts.add((String) expertUsername))).
                onFailure(response -> {
                    throw new RuntimeException(response.getMessage());
                });

        return getAssetForecastsFromExperts(assetName, experts);
    }

    private List<Forecast> getAssetForecastsFromExperts(String asset, List<String> expertsUsernames) {
        List<Forecast> forecasts = new LinkedList<>();
        expertsUsernames.forEach(username -> forecasts.addAll(forecastRepository.findUsersForecastsForAsset(username, asset)));
        return forecasts;
    }
}
