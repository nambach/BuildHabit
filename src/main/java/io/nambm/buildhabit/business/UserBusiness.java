package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.user.UserModel;

public interface UserBusiness extends GenericBusiness<UserModel> {

    UserModel get(String username);
}
