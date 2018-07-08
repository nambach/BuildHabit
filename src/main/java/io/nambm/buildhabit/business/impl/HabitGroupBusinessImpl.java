package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.HabitGroupBusiness;
import io.nambm.buildhabit.entity.HabitGroupEntity;
import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;
import org.springframework.stereotype.Service;

@Service
public class HabitGroupBusinessImpl extends GenericBusinessImpl<HabitGroupModel, HabitGroupEntity> implements HabitGroupBusiness {

    @Override
    public HabitGroupModel get(String groupId) {

        HabitGroupModel wrapper = new HabitGroupModel();
        wrapper.setGroupId(groupId);

        return get(wrapper);
    }
}
