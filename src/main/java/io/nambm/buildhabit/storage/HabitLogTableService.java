package io.nambm.buildhabit.storage;

import io.nambm.buildhabit.entity.HabitLogEntity;

import java.util.List;

public interface HabitLogTableService {

    boolean insert(HabitLogEntity entity);

    boolean update(HabitLogEntity entity);

    boolean remove(HabitLogEntity entity);

    HabitLogEntity get(String partitionKey, String rowKey);

    List<HabitLogEntity> getAllHabitLogs(String username, String equalConditions, String queryFilter);
}
