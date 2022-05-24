package com.expert.mark.service;

import com.expert.mark.model.content.forecast.Forecast;

import java.util.List;

public interface ForecastRecommendationService {
    List<Forecast> recommendForecastForUser(String username);
}
