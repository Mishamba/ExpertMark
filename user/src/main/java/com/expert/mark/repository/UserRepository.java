package com.expert.mark.repository;

import com.expert.mark.model.account.User;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User update(User user);
    User getUserByUsernameWithoutProfile(String username);
    User getUserByUsernameWithProfile(String username);
    List<String> getUserFollowings(String username);
    List<String> getMostTrustedExpertsUsernames();
    boolean addFollowing(String username, String followingUsername);
    boolean removeFollowing(String username, String followingUsername);
}
