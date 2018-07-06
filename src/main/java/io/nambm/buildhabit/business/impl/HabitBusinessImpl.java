package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.entity.HabitEntity;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.storage.HabitTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitBusinessImpl implements HabitBusiness {

    private final HabitTableService habitTableService;

    @Autowired
    public HabitBusinessImpl(HabitTableService habitTableService) {
        this.habitTableService = habitTableService;
    }

    @Override
    public boolean insert(HabitModel model) {
        habitTableService.insert(model.toEntity());
        return true;
    }

    @Override
    public boolean update(HabitModel model) {
        return habitTableService.update(model.toEntity());
    }

    @Override
    public boolean remove(String username, String id) {
        HabitModel wrapper = wrap(username, id);

        HabitEntity entity = habitTableService.get(wrapper.getPartitionKey(), wrapper.getRowKey());
        return habitTableService.remove(entity);
    }

    @Override
    public HabitModel get(HabitModel model) {
        HabitEntity entity = habitTableService.get(model.getPartitionKey(), model.getRowKey());
        if (entity != null) {
            return entity.toModel();
        } else {
            return null;
        }
    }

    @Override
    public HabitModel get(String username, String id) {
        HabitModel wrapper = wrap(username, id);

        HabitEntity entity = habitTableService.get(wrapper.getPartitionKey(), wrapper.getRowKey());
        if (entity != null) {
            return entity.toModel();
        } else {
            return null;
        }
    }

    @Override
    public List<HabitModel> getAllHabits(String username, String equalConditions) {
        return habitTableService.getAllHabits(username, equalConditions)
                .stream()
                .map(HabitEntity::toModel)
                .collect(Collectors.toList());
    }

    private HabitModel wrap(String username, String id) {
        HabitModel wrapper = new HabitModel();
        wrapper.setId(id);
        wrapper.setUsername(username);
        return wrapper;
    }
}
