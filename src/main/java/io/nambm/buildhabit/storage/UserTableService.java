package io.nambm.buildhabit.storage;

import io.nambm.buildhabit.entity.UserEntity;
import io.nambm.buildhabit.model.submodel.BootgridResponse;

import java.util.List;

public interface UserTableService {
    boolean insert(UserEntity entity);

    boolean update(UserEntity entity);

    boolean remove(UserEntity entity);

    UserEntity get(String username);

    List<UserEntity> getAll(String equalConditions);

    BootgridResponse<UserEntity> getPage(int rowCount, int currentPage, String partitionKey, String queryFilter);
}
