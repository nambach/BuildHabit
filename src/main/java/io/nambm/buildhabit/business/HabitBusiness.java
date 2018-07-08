package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.habit.HabitModel;

public interface HabitBusiness extends GenericBusiness<HabitModel> {

    HabitModel get(String username, String habitId);
}
