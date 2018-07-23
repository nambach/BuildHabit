package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.user.FollowModel;
import org.springframework.http.HttpStatus;

public interface FollowBusiness extends GenericBusiness<FollowModel> {

    FollowModel getByUsername(String username);

    HttpStatus updateFollowings(FollowModel model);

    HttpStatus updateFollowers(FollowModel model);
}
