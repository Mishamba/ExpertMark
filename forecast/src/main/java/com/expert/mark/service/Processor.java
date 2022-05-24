package com.expert.mark.service;

public interface Processor {
    void processForecastsForUser(String username);
    void updateExpertStatisticsAndCalculateForecastAccuracy();
}
