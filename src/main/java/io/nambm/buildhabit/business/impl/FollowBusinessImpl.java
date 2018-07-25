package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.FollowBusiness;
import io.nambm.buildhabit.entity.FollowEntity;
import io.nambm.buildhabit.model.user.FollowModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FollowBusinessImpl extends GenericBusinessImpl<FollowModel, FollowEntity> implements FollowBusiness {

    @Override
    public FollowModel getByUsername(String username) {
        FollowModel model = new FollowModel();
        model.setUsername(username);
        return get(model);
    }

    @Override
    public HttpStatus updateFollowings(FollowModel model) {
        return update(model, "followings");
    }

    @Override
    public HttpStatus updateFollowers(FollowModel model) {
        return update(model, "followers");
    }
}