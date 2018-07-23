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

        FollowModel userToTarget = new FollowModel();
        userToTarget.setUsername(user.getUsername());
        userToTarget.follow(target);

        followBusiness.insert(userToTarget);

        FollowModel celebrity = new FollowModel();
        celebrity.setUsername(target.getUsername());
        celebrity.addFollower(user);

        followBusiness.insert(celebrity);

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus unfollow(UserModel user, UserModel target) {
        FollowModel userToTarget = followBusiness.getByUsername(user.getUsername());
        if (userToTarget != null) {
            userToTarget.unfollow(target);
            followBusiness.update(userToTarget);
        }

        FollowModel celebrity = followBusiness.getByUsername(target.getUsername());
        if (celebrity != null) {
            celebrity.removeFollower(user);
            followBusiness.update(celebrity);
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
