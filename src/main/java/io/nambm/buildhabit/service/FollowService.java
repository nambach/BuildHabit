package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.user.FollowModel;
import io.nambm.buildhabit.model.user.UserModel;
import org.springframework.http.HttpStatus;

public interface FollowService {

    HttpStatus follow(UserModel user, UserModel target);

    HttpStatus unfollow(UserModel user, UserModel target);

    FollowModel getUserFollow(String username);
}
