package io.nambm.buildhabit.model.habitgroup;

import com.google.gson.Gson;
import io.nambm.buildhabit.entity.HabitGroupEntity;
import io.nambm.buildhabit.model.GenericModel;

import java.util.List;

public class HabitGroupModel implements GenericModel<HabitGroupEntity> {

    private String groupId;
    private String rootHabit;
    private List<String> habits;

    public HabitGroupModel() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRootHabit() {
        return rootHabit;
    }

    public void setRootHabit(String rootHabit) {
        this.rootHabit = rootHabit;
    }

    public List<String> getHabits() {
        return habits;
    }

    public void setHabits(List<String> habits) {
        this.habits = habits;
    }

    public HabitGroupEntity toEntity() {
        HabitGroupEntity entity = new HabitGroupEntity();

        entity.setGroupId(this.groupId);
        entity.setRootHabit(this.rootHabit);
        entity.setHabits(new Gson().toJson(this.habits));

        entity.setPartitionKey(this.getPartitionKey());
        entity.setRowKey(this.getRowKey());

        return entity;
    }

    public String getPartitionKey() {
        return groupId;
    }

    public String getRowKey() {
        return groupId;
    }
}
