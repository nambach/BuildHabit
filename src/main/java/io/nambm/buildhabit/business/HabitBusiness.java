package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.habit.HabitModel;

import java.util.List;

public interface HabitBusiness {

    boolean insert(HabitModel model);

    boolean update(HabitModel model);

    boolean remove(HabitModel model);

    List<HabitModel> getAllHabits(String username, String equalConditions);
}
