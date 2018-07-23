package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.FollowController;
import io.nambm.buildhabit.model.user.FollowModel;
import io.nambm.buildhabit.model.user.UserModel;
import io.nambm.buildhabit.service.FollowService;
import io.nambm.buildhabit.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class FollowControllerImpl implements FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<String> follow(String username, String targetUsername) {
        UserModel user = new UserModel();
        user.setUsername(username);

        UserModel target = new UserModel();
        target.setUsername(targetUsername);

        HttpStatus status = followService.follow(user, target);
        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollow(String username, String targetUsername) {
        UserModel user = new UserModel();
        user.setUsername(username);

        UserModel target = new UserModel();
        target.setUsername(targetUsername);

        HttpStatus status = followService.unfollow(user, target);
        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserModel>> getFollowers(String username) {
        FollowModel followModel = followService.getUserFollow(username);
        List<UserModel> models = followModel.getFollowers()
                .stream()
                .map(flw -> {
                    UserModel userModel = new UserModel();
                    userModel.setUsername(flw.getUsername());
                    return userModel;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @GetMapping("/followings")
    public ResponseEntity<List<UserModel>> getFollowings(String username) {
        FollowModel followModel = followService.getUserFollow(username);
        List<UserModel> models = followModel.getFollowings()
                .stream()
                .map(flw -> {
                    UserModel userModel = new UserModel();
                    userModel.setUsername(flw.getUsername());
                    return userModel;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(models, HttpStatus.OK);
    }
}
