package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;

public interface HabitGroupBusiness extends GenericBusiness<HabitGroupModel> {

    HabitGroupModel get(String groupId);
}
