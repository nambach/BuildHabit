package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.UserBusiness;
import io.nambm.buildhabit.entity.UserEntity;
import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.model.user.UserModel;
import io.nambm.buildhabit.storage.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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
    public boolean update(UserModel model, String... properties) {
        UserEntity currentEntity = userTableService.get(model.getUsername());
        if (currentEntity != null) {
            UserModel currentModel = currentEntity.toModel();

            for (String property : properties) {
                try {
                    Field field = UserModel.class.getDeclaredField(property);
                    field.setAccessible(true);
                    field.set(currentModel, field.get(model));
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }

            userTableService.update(currentModel.toEntity());
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
    public List<UserModel> getAll(String equalConditions) {
        return userTableService.getAll(equalConditions).stream()
                .map(UserEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public BootgridResponse<UserModel> getPage(int rowCount, int currentPage) {
        BootgridResponse<UserEntity> entities = userTableService.getPage(rowCount, currentPage, null, null);
        BootgridResponse<UserModel> models = new BootgridResponse<>(
                entities.getCurrent(),
                entities.getRowCount(),
                entities.getTotal(),
                entities.getRows().stream().map(UserEntity::toModel).collect(Collectors.toList())
        );

        return models;
    }
}
