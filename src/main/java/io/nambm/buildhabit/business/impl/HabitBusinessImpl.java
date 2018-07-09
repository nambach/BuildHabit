package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.entity.HabitEntity;
import io.nambm.buildhabit.model.habit.HabitModel;
import org.springframework.stereotype.Service;

@Service
public class HabitBusinessImpl extends GenericBusinessImpl<HabitModel, HabitEntity> implements HabitBusiness {

    @Override
    public HabitModel get(String username, String habitId) {
        HabitModel model = new HabitModel();
        model.setId(habitId);
        model.setUsername(username);
        return get(model);
    }

    @Override
    public HabitModel get(String habitId) {
        HabitModel model = new HabitModel();
        model.setId(habitId);

        return get(model, true);
    }
}