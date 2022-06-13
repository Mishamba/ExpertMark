package com.expert.mark.service.impl;

import com.expert.mark.model.account.User;
import com.expert.mark.model.account.expert.ExpertStatistic;
import com.expert.mark.repository.ExpertStatisticRepository;
import com.expert.mark.repository.UserRepository;
import com.expert.mark.repository.impl.ExpertStatisticRepositoryImpl;
import com.expert.mark.repository.impl.UserRepositoryImpl;
import com.expert.mark.service.UserService;
import io.vertx.core.json.JsonObject;

import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ExpertStatisticRepository expertStatisticRepository = new ExpertStatisticRepositoryImpl();

    @Override
    public User createUser(User user) {
        user.setCreateDate(new Date());
        ExpertStatistic expertStatistic = new ExpertStatistic(new JsonObject().put("expertUsername", user.getUsername()));
        expertStatisticRepository.saveExpertStatistic(expertStatistic);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public User getUserByUsernameWithoutProfile(String username) {
        return userRepository.getUserByUsernameWithoutProfile(username);
    }

    @Override
    public User getUserByUsernameWithProfile(String username) {
        return userRepository.getUserByUsernameWithProfile(username);
    }

    @Override
    public List<String> getUserFollowings(String username) {
        return userRepository.getUserFollowings(username);
    }

    @Override
    public List<String> getMostTrustedExpertsUsernames() {
        return userRepository.getMostTrustedExpertsUsernames();
    }

    @Override
    public boolean addFollowing(String username, String followingUsername) {
        return userRepository.addFollowing(username, followingUsername);
    }

    @Override
    public boolean removeFollowing(String username, String followingUsername) {
        return userRepository.removeFollowing(username, followingUsername);
    }
}
