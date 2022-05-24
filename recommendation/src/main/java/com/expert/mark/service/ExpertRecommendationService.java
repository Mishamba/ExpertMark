package com.expert.mark.service;

import com.expert.mark.model.account.User;

import java.util.List;

public interface ExpertRecommendationService {
    List<User> recommendExpertsForUser(String username);
}
