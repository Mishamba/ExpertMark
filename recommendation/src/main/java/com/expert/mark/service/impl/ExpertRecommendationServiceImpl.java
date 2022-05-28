package com.expert.mark.service.impl;

import com.expert.mark.model.account.User;
import com.expert.mark.service.ExpertRecommendationService;
import com.expert.mark.service.BaseRecommendationService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExpertRecommendationServiceImpl extends BaseRecommendationService implements ExpertRecommendationService {
    @Override
    public List<User> recommendExpertsForUser(String username) {
        Set<String> assetNames = this.getAssetNamesForUser(username);

        List<String> userFollowings = this.userRepository.getUserFollowings(username);

        List<String> recommendationUsernames = new LinkedList<>();

        assetNames.forEach(assetName -> forecastRepository.findForecastsForAssetWhereOwnerNotIn(assetName, userFollowings).forEach(forecast -> {
            Map<String, Integer> assetForecastsQuantity = new HashMap<>();
            assetNames.forEach(anotherAssetName -> assetForecastsQuantity.put(anotherAssetName, 0));
            AtomicInteger forecastsQuantity = new AtomicInteger();
            forecastRepository.findUsersForecasts(forecast.getOwnerUsername()).forEach(usersForecasts -> {
                String currentAssetName = forecast.getAssetName();
                forecastsQuantity.getAndIncrement();
                if (assetForecastsQuantity.containsKey(currentAssetName)) {
                    assetForecastsQuantity.put(currentAssetName, assetForecastsQuantity.get(currentAssetName) + 1);
                }
            });

            float minimalPercent = 10F;

            if (assetNames.size() > 10) minimalPercent = 100 / (1.5F * forecastsQuantity.get());

            for (String asset : assetForecastsQuantity.keySet()) {
                int assetForecastQuantity = assetForecastsQuantity.get(asset);
                float assetForecastsPercent = assetForecastQuantity * (100F / forecastsQuantity.get());

                if (assetForecastsPercent >= minimalPercent) {
                    recommendationUsernames.add(forecast.getOwnerUsername());
                }
            }
        }));

        List<User> usersToRecommend = new LinkedList<>();
        recommendationUsernames.remove(username);

        recommendationUsernames.forEach(currentUsername -> usersToRecommend.add(userRepository.getUserByUsernameWithProfile(currentUsername)));

        return usersToRecommend;
    }
}
