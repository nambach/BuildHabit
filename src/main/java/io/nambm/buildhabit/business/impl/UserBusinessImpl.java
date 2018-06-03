package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.UserBusiness;
import io.nambm.buildhabit.entity.UserEntity;
import io.nambm.buildhabit.model.UserModel;
import io.nambm.buildhabit.storage.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBusinessImpl implements UserBusiness {

    private final UserTableService userTableService;

    @Autowired
    public UserBusinessImpl(UserTableService userTableService) {
        this.userTableService = userTableService;
    }

    @Override
    public boolean insert(UserModel model) {
        if (userTableService.get(model.getUsername()) == null) {
            userTableService.insert(model.toEntity());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(UserModel model) {
        if (userTableService.get(model.getUsername()) != null) {
            userTableService.update(model.toEntity());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(UserModel model) {
        return userTableService.remove(model.toEntity());
    }

    @Override
    public UserModel get(String username) {
        UserEntity entity = userTableService.get(username);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public List<UserModel> getAll(int n) {
        return userTableService.getAll(n).stream()
                .map(UserEntity::toModel)
                .collect(Collectors.toList());
    }
}
