package io.nambm.buildhabit.storage.impl;

import io.nambm.buildhabit.entity.HabitEntity;
import io.nambm.buildhabit.storage.HabitTableService;
import io.nambm.buildhabit.table.impl.TableServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitTableServiceImpl extends TableServiceImpl<HabitEntity> implements HabitTableService {

    @Override
    public boolean insert(HabitEntity entity) {
        return insertOrReplace(entity);
    }

    @Override
    public boolean update(HabitEntity entity) {
        return insertOrReplace(entity);
    }

    @Override
    public boolean remove(HabitEntity entity) {
        return delete(entity) != null;
    }

    @Override
    public HabitEntity get(String partitionKey, String rowKey) {
        return this.getEntity(partitionKey, rowKey);
    }

    @Override
    public List<HabitEntity> getAllHabits(String username, String equalConditions) {
        return searchAll(username, equalConditions);
    }
}
