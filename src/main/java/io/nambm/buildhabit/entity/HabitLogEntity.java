package io.nambm.buildhabit.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.table.annotation.AzureTableName;
import io.nambm.buildhabit.util.date.Day;

import java.util.List;

@AzureTableName("habitlog")
public class HabitLogEntity extends GenericEntity<HabitLogModel> {

    // PartitionKey = [username]
    // RowKey = [month][year]_[habitId]

    private String times;

    // MonthInfo = [year][-][month]
    // Eg: 2010-01
    private String monthInfo;
    private String habitId;
    private String username;

    public HabitLogEntity() {
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getMonthInfo() {
        return monthInfo;
    }

    public void setMonthInfo(String monthInfo) {
        this.monthInfo = monthInfo;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HabitLogModel toModel() {
        HabitLogModel model = new HabitLogModel();

        Gson gson = new Gson();

        model.setTimes(gson.fromJson(times, new TypeToken<List<Long>>(){}.getType()));
        model.setMonthInfo(fromMonthInfo(monthInfo));
        model.setHabitId(habitId);
        model.setUsername(getUsername());

        return model;
    }

    private static Day fromMonthInfo(String monthInfo) {
        int year = 0;
        int month = 0;
        try {
            // Eg: monthInfo = '2010-01'
            year = Integer.parseInt(monthInfo.substring(0, 4));
            month = Integer.parseInt(monthInfo.substring(5));
        } catch (NumberFormatException ignored) {
        }

        Day day = new Day();
        day.year = year;
        day.month = month;

        return day;
    }
}
