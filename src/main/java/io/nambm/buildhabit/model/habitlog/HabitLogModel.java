package io.nambm.buildhabit.model.habitlog;

import com.google.gson.Gson;
import io.nambm.buildhabit.entity.HabitLogEntity;
import io.nambm.buildhabit.util.date.Day;

import java.util.List;

public class HabitLogModel {

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

    public HabitLogEntity toEntity() {
        HabitLogEntity entity = new HabitLogEntity();
        Gson gson = new Gson();

        String rowKey = getRowKey(
                this.monthInfo.month,
                this.monthInfo.year,
                this.habitId
        );

        // Set entity ID
        entity.setPartitionKey(username);
        entity.setRowKey(rowKey);

        // Set main info
        entity.setTimes(gson.toJson(times));

        // Set additional info
        entity.setMonthInfo(gson.toJson(monthInfo));
        entity.setHabitId(habitId);
        entity.setUsername(username);

        return entity;
    }

    public static String getRowKey(int month, int year, String habitId) {
        return habitId + "_" + String.format("%04d%02d", year, month);
    }

    public static String getRowKey(HabitLogModel model) {
        return model.habitId + "_" + String.format("%04d%02d", model.monthInfo.year, model.monthInfo.month);
    }

    public static String getPartitionKey(HabitLogModel model) {
        return model.getUsername();
    }
}
