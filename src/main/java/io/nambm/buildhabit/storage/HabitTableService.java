package io.nambm.buildhabit.storage;

import io.nambm.buildhabit.entity.HabitEntity;

import java.util.List;

public interface HabitTableService {
    boolean insert(HabitEntity entity);

    boolean update(HabitEntity entity);

    boolean remove(HabitEntity entity);

    List<HabitEntity> getAllHabits(String username, String equalConditions);

}
