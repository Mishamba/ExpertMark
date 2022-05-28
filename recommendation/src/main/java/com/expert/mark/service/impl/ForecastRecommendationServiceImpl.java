package com.expert.mark.service.impl;

import com.expert.mark.model.content.forecast.Forecast;
import com.expert.mark.service.BaseRecommendationService;
import com.expert.mark.service.ForecastRecommendationService;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ForecastRecommendationServiceImpl extends BaseRecommendationService implements ForecastRecommendationService {
    @Override
    public List<Forecast> recommendForecastForUser(String username) {
        Set<String> assetNames = this.getAssetNamesForUser(username);

        List<String> userFollowings = this.userRepository.getUserFollowings(username);

        userFollowings.add(username);
        List<Forecast> recommendedForecasts = new LinkedList<>();
        assetNames.forEach(x -> recommendedForecasts.addAll(forecastRepository.findForecastsForAssetWhereOwnerNotIn(x, userFollowings)));

        return recommendedForecasts;
    }
}
