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
        return insertOrMerge(entity);
    }

    @Override
    public boolean remove(HabitEntity entity) {
        return delete(entity) != null;
    }

    @Override
    public List<HabitEntity> getAllHabits(String username, String equalConditions) {
        return searchByPartition(username, equalConditions);
    }
}
