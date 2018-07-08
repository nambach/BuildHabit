package io.nambm.buildhabit.model.habitlog;

import com.google.gson.Gson;
import io.nambm.buildhabit.entity.HabitLogEntity;
import io.nambm.buildhabit.model.GenericModel;
import io.nambm.buildhabit.util.date.Day;

import java.util.List;

public class HabitLogModel implements GenericModel<HabitLogEntity> {

    // Main info
    private List<Long> times;

    // Additional info
    private Day monthInfo;
    private String habitId;
    private String username;

    public HabitLogModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Day getMonthInfo() {
        return monthInfo;
    }

    public void setMonthInfo(Day monthInfo) {
        this.monthInfo = monthInfo;
    }

    public List<Long> getTimes() {
        return times;
    }

    public void setTimes(List<Long> times) {
        this.times = times;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    @Override
    public String getPartitionKey() {
        return this.username;
    }

    @Override
    public String getRowKey() {
        return this.habitId + "_" + getMonthInfo(this.monthInfo);
    }

    public HabitLogEntity toEntity() {
        HabitLogEntity entity = new HabitLogEntity();

        // Set entity ID
        entity.setPartitionKey(getPartitionKey());
        entity.setRowKey(getRowKey());

        // Set main info
        entity.setTimes(new Gson().toJson(times));

        // Set additional info
        entity.setMonthInfo(getMonthInfo(monthInfo));
        entity.setHabitId(habitId);
        entity.setUsername(username);

        return entity;
    }

    public static String getMonthInfo(Day day) {
        return String.format("%04d-%02d", day.year, day.month);
    }
}
