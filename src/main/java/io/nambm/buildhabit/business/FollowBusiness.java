package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.user.FollowModel;

public interface FollowBusiness extends GenericBusiness<FollowModel> {

    FollowModel getByUsername(String username);
}
