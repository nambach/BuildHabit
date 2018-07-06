package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.HabitGroupBusiness;
import io.nambm.buildhabit.entity.HabitGroupEntity;
import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;
import io.nambm.buildhabit.storage.HabitGroupTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HabitGroupBusinessImpl implements HabitGroupBusiness {

    private final HabitGroupTableService habitGroupTableService;

    @Autowired
    public HabitGroupBusinessImpl(HabitGroupTableService habitGroupTableService) {
        this.habitGroupTableService = habitGroupTableService;
    }

    @Override
    public boolean insert(HabitGroupModel model) {
        return habitGroupTableService.insert(model.toEntity());
    }

    @Override
    public boolean update(HabitGroupModel model) {
        return habitGroupTableService.update(model.toEntity());
    }

    @Override
    public boolean remove(HabitGroupModel model) {
        HabitGroupEntity entity = habitGroupTableService.get(model.getPartitionKey(), model.getRowKey());
        if (entity != null) {
            return habitGroupTableService.remove(entity);
        } else {
            return false;
        }
    }

    @Override
    public HabitGroupModel get(String groupId) {

        HabitGroupModel wrapper = new HabitGroupModel();
        wrapper.setGroupId(groupId);

        HabitGroupEntity entity = habitGroupTableService.get(wrapper.getPartitionKey(), wrapper.getRowKey());
        if (entity != null) {
            return entity.toModel();
        } else {
            return null;
        }
    }
}
