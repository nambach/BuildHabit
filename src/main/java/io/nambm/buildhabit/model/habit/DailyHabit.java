package io.nambm.buildhabit.model.habit;

import io.nambm.buildhabit.util.date.Day;

import java.util.ArrayList;
import java.util.List;

public class DailyHabit {
    private Day day;
    private List<DailyHabitModel> habits;

    public DailyHabit(Day day, boolean initEmptyList) {
        this.day = day;
        if (initEmptyList) {
            this.habits = new ArrayList<>();
        }
    }

    public DailyHabit(Day day, List<DailyHabitModel> habits) {
        this.day = day;
        this.habits = habits;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public List<DailyHabitModel> getHabits() {
        return habits;
    }

    public void setHabits(List<DailyHabitModel> habits) {
        this.habits = habits;
    }
}
