package io.nambm.buildhabit.model.habitlog;

import io.nambm.buildhabit.model.habit.HabitModel;

import java.util.List;

public class StatisticResponse {

    private HabitModel rootHabit;
    private List<HabitModel> habitMembers;
    private List<StatisticEntry> logs;

    public StatisticResponse() {
    }

    public StatisticResponse(HabitModel rootHabit, List<HabitModel> habitMembers, List<StatisticEntry> logs) {
        this.rootHabit = rootHabit;
        this.habitMembers = habitMembers;
        this.logs = logs;
    }

    public HabitModel getRootHabit() {
        return rootHabit;
    }

    public void setRootHabit(HabitModel rootHabit) {
        this.rootHabit = rootHabit;
    }

    public List<HabitModel> getHabitMembers() {
        return habitMembers;
    }

    public void setHabitMembers(List<HabitModel> habitMembers) {
        this.habitMembers = habitMembers;
    }

    public List<StatisticEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<StatisticEntry> logs) {
        this.logs = logs;
    }

    public static class StatisticEntry {
        public long time;
        public boolean done;

        public StatisticEntry(long time, boolean done) {
            this.time = time;
            this.done = done;
        }
    }
}
