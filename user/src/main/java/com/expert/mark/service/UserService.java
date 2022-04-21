package com.expert.mark.service;

import com.expert.mark.model.account.Profile;
import com.expert.mark.model.account.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(User user);
    User getUserByUsername(String username);
    User getUserByUsernameWithProfile(String username);
    Profile getUserProfileByUsername(String username);
    List<String> getUserFollowings(String username);
    List<String> getMostTrustedExpertsUsernames();
    boolean addFollowing(String username, String followingUsername);
    boolean removeFollowing(String username, String followingUsername);
}
