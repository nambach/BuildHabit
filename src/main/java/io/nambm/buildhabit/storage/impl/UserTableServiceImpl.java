package io.nambm.buildhabit.storage.impl;

import io.nambm.buildhabit.entity.UserEntity;
import io.nambm.buildhabit.storage.UserTableService;
import io.nambm.buildhabit.table.impl.TableServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTableServiceImpl extends TableServiceImpl<UserEntity> implements UserTableService {
    @Override
    public boolean insert(UserEntity entity) {
        return insertOrReplace(entity);
    }

    @Override
    public boolean update(UserEntity entity) {
        return insertOrMerge(entity);
    }

    @Override
    public boolean remove(UserEntity entity) {
        return delete(entity) != null;
    }

    @Override
    public UserEntity get(String username) {
        return search(username, username);
    }

    @Override
    public List<UserEntity> getAll(int n) {
        return searchTop("namebm", n);
    }
}
