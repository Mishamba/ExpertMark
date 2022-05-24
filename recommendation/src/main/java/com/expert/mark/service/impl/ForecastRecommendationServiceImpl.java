package com.expert.mark.service.impl;

import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.BaseRecommendationProcessor;
import com.expert.mark.service.ForecastRecommendationService;

import java.util.LinkedList;
import java.util.List;

public class ForecastRecommendationServiceImpl extends BaseRecommendationProcessor implements ForecastRecommendationService {
    @Override
    public List<Forecast> recommendForecastForUser(String username) {
        List<String> assetNames = this.getAssetNamesForUser(username);

        List<String> userFollowings = this.userRepository.getUserFollowings(username);

        List<Forecast> recommendedForecasts = new LinkedList<>();
        assetNames.forEach(x -> forecastRepository.findForecastsForAssetWhereOwnerNotIn(x, userFollowings));

        return recommendedForecasts;
    }
}
