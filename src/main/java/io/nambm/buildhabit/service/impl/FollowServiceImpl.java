package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.FollowBusiness;
import io.nambm.buildhabit.model.user.FollowModel;
import io.nambm.buildhabit.model.user.UserModel;
import io.nambm.buildhabit.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowBusiness followBusiness;

    @Autowired
    public FollowServiceImpl(FollowBusiness followBusiness) {
        this.followBusiness = followBusiness;
    }

    @Override
    public HttpStatus follow(UserModel user, UserModel target) {
        // Add relationship between user and following target
        FollowModel userToTarget = followBusiness.getByUsername(user.getUsername());
        if (userToTarget != null) {
            userToTarget.follow(target);
            followBusiness.updateFollowings(userToTarget);
        } else {
            userToTarget = new FollowModel();
            userToTarget.setUsername(user.getUsername());
            userToTarget.follow(target);
            followBusiness.insert(userToTarget);
        }

        // Add reverse relationship between following target and user
        FollowModel celebrity = followBusiness.getByUsername(target.getUsername());
        if (celebrity != null) {
            celebrity.addFollower(user);
            followBusiness.updateFollowers(celebrity);
        } else {
            celebrity = new FollowModel();
            celebrity.setUsername(target.getUsername());
            celebrity.addFollower(user);
            followBusiness.insert(celebrity);
        }

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus unfollow(UserModel user, UserModel target) {
        FollowModel userToTarget = followBusiness.getByUsername(user.getUsername());
        if (userToTarget != null) {
            userToTarget.unfollow(target);
            followBusiness.updateFollowings(userToTarget);
        }

        FollowModel celebrity = followBusiness.getByUsername(target.getUsername());
        if (celebrity != null) {
            celebrity.removeFollower(user);
            followBusiness.updateFollowers(celebrity);
        }

        return HttpStatus.OK;
    }

    @Override
    public FollowModel getUserFollow(String username) {
        FollowModel model = new FollowModel();
        model.setUsername(username);

        return followBusiness.get(model);
    }
}
