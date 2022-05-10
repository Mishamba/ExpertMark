package com.expert.mark.repository.impl;

import com.expert.mark.model.account.User;
import com.expert.mark.repository.UserRepository;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
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
