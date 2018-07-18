package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.UserBusiness;
import io.nambm.buildhabit.entity.UserEntity;
import io.nambm.buildhabit.model.user.UserModel;
import org.springframework.stereotype.Service;

@Service
public class UserBusinessImpl extends GenericBusinessImpl<UserModel, UserEntity> implements UserBusiness {

    @Override
    public UserModel get(String username) {
        UserModel wrapper = new UserModel();
        wrapper.setUsername(username);
        return get(wrapper);
    }
}
