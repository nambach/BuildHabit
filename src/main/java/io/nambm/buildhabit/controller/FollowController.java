package io.nambm.buildhabit.controller;

import io.nambm.buildhabit.model.user.UserModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FollowController {

    ResponseEntity<String> follow(String username, String targetUsername);

    ResponseEntity<String> unfollow(String username, String targetUsername);

    ResponseEntity<List<UserModel>> getFollowers(String username);

    ResponseEntity<List<UserModel>> getFollowings(String username);
}
