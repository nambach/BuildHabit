package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.entity.HabitEntity;
import io.nambm.buildhabit.model.HabitModel;
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
        return false;
    }

    @Override
    public boolean remove(HabitModel model) {
        return false;
    }

    @Override
    public List<HabitModel> getAllHabits(String username, String equalConditions) {
        return habitTableService.getAllHabits(username, equalConditions)
                .stream()
                .map(HabitEntity::toModel)
                .collect(Collectors.toList());
    }
}
