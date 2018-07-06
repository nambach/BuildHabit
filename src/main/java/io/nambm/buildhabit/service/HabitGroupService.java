package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;

public interface HabitGroupService {

    HabitGroupModel createGroup(String habitId);

    void addHabitToGroup(String habitId, String groupId);
}
