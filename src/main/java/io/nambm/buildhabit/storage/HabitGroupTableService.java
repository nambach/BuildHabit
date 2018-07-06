package io.nambm.buildhabit.storage;

import io.nambm.buildhabit.entity.HabitGroupEntity;

public interface HabitGroupTableService {

    boolean insert(HabitGroupEntity entity);

    boolean update(HabitGroupEntity entity);

    boolean remove(HabitGroupEntity entity);

    HabitGroupEntity get(String partitionKey, String rowKey);
}
