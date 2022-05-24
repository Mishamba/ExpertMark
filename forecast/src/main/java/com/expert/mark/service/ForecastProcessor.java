package com.expert.mark.service;

public interface ForecastProcessor {
    void processForecastsForUser(String username);
    void updateExpertStatisticsAndCalculateForecastAccuracy();
}
