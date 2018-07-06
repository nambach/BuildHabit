package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;

public interface HabitGroupBusiness {

    boolean insert(HabitGroupModel model);

    boolean update(HabitGroupModel model);

    boolean remove(HabitGroupModel model);

    HabitGroupModel get(String groupId);
}
