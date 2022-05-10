package com.expert.mark.service.impl;

import com.expert.mark.model.account.Profile;
import com.expert.mark.model.account.User;
import com.expert.mark.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User getUserByUsernameWithProfile(String username) {
        return null;
    }

    @Override
    public Profile getUserProfileByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getUserFollowings(String username) {
        return null;
    }

    @Override
    public List<String> getMostTrustedExpertsUsernames() {
        return null;
    }

    @Override
    public boolean addFollowing(String username, String followingUsername) {
        return false;
    }

    @Override
    public boolean removeFollowing(String username, String followingUsername) {
        return false;
    }
}
