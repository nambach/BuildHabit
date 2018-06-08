package io.nambm.buildhabit.storage.impl;

import io.nambm.buildhabit.entity.HabitLogEntity;
import io.nambm.buildhabit.storage.HabitLogTableService;
import io.nambm.buildhabit.table.impl.TableServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitLogTableServiceImpl extends TableServiceImpl<HabitLogEntity> implements HabitLogTableService {

    @Override
    public boolean insert(HabitLogEntity entity) {
        return insertOrReplace(entity);
    }

    @Override
    public boolean update(HabitLogEntity entity) {
        return insertOrReplace(entity);
    }

    @Override
    public boolean remove(HabitLogEntity entity) {
        return delete(entity) != null;
    }

    @Override
    public HabitLogEntity get(String partitionKey, String rowKey) {
        return getEntity(partitionKey, rowKey);
    }

    @Override
    public List<HabitLogEntity> getAllHabitLogs(String username, String equalConditions, String queryFilter) {
        return searchAll(username, equalConditions, queryFilter);
    }
}
