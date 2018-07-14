package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitGroupBusiness;
import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;
import io.nambm.buildhabit.service.HabitGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class HabitGroupServiceImpl implements HabitGroupService {

    private final HabitGroupBusiness habitGroupBusiness;

    @Autowired
    public HabitGroupServiceImpl(HabitGroupBusiness habitGroupBusiness) {
        this.habitGroupBusiness = habitGroupBusiness;
    }

    @Override
    public HabitGroupModel createGroup(String habitId) {
        HabitGroupModel groupModel = new HabitGroupModel();

        groupModel.setGroupId(habitId);
        groupModel.setRootHabit(habitId);

        List<String> habits = new LinkedList<>();
        habits.add(habitId);
        groupModel.setHabits(habits);

        habitGroupBusiness.insert(groupModel);

        return groupModel;
    }

    @Override
    public void addHabitToGroup(String habitId, String groupId) {
        HabitGroupModel groupModel = habitGroupBusiness.get(groupId);
        if (groupModel != null && groupModel.getHabits() != null) {
            if (groupModel.getHabits().indexOf(habitId) == -1) {
                groupModel.getHabits().add(habitId);
            }
        }
        habitGroupBusiness.update(groupModel);
    }
}
