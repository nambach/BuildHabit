package io.nambm.buildhabit.storage.impl;

import io.nambm.buildhabit.entity.HabitGroupEntity;
import io.nambm.buildhabit.storage.HabitGroupTableService;
import io.nambm.buildhabit.table.impl.TableServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HabitGroupTableServiceImpl extends TableServiceImpl<HabitGroupEntity> implements HabitGroupTableService {

    @Override
    public boolean insert(HabitGroupEntity entity) {
        return insertOrReplace(entity);
    }

    @Override
    public boolean update(HabitGroupEntity entity) {
        return insertOrReplace(entity);
    }

    @Override
    public boolean remove(HabitGroupEntity entity) {
        return delete(entity) != null;
    }

    @Override
    public HabitGroupEntity get(String partitionKey, String rowKey) {
        return getEntity(partitionKey, rowKey);
    }
}
