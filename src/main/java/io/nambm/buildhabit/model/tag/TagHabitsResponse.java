package io.nambm.buildhabit.model.tag;

import io.nambm.buildhabit.model.habit.HabitModel;

import java.util.List;

public class TagHabitsResponse {
    private String tagName;
    private List<HabitModel> habits;

    public TagHabitsResponse() {
    }

    public TagHabitsResponse(String tagName, List<HabitModel> habits) {
        this.tagName = tagName;
        this.habits = habits;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<HabitModel> getHabits() {
        return habits;
    }

    public void setHabits(List<HabitModel> habits) {
        this.habits = habits;
    }
}
