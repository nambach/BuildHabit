package io.nambm.buildhabit.entity;

import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;
import io.nambm.buildhabit.table.annotation.AzureTableName;
import io.nambm.buildhabit.util.JsonUtils;

@AzureTableName("habitgroup")
public class HabitGroupEntity extends GenericEntity<HabitGroupModel> {

    private String groupId;
    private String rootHabit;
    private String habits;

    public HabitGroupEntity() {
    }

    public String getRootHabit() {
        return rootHabit;
    }

    public void setRootHabit(String rootHabit) {
        this.rootHabit = rootHabit;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public HabitGroupModel toModel() {
        HabitGroupModel model = new HabitGroupModel();

        model.setGroupId(this.groupId);
        model.setRootHabit(this.rootHabit);
        model.setHabits(JsonUtils.getArray(this.habits, String.class));

        return model;
    }
}
