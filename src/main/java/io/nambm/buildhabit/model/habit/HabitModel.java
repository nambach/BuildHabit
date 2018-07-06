package io.nambm.buildhabit.model.habit;

import io.nambm.buildhabit.entity.HabitEntity;
import io.nambm.buildhabit.util.JsonUtils;

import java.util.Arrays;
import java.util.List;

public class HabitModel {
    private String username;
    private String id;

    // contents
    private String title;
    private String description;
    private String icon;

    private Schedule schedule;
    private List<String> tags;
    private List<Long> logs;

    private Long startTime;
    private Long endTime;

    public HabitModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Long> getLogs() {
        return logs;
    }

    public void setLogs(List<Long> logs) {
        this.logs = logs;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public HabitEntity toEntity() {
        HabitEntity entity = new HabitEntity();

        entity.setPartitionKey(getPartitionKey(this));
        entity.setRowKey(getRowKey(this));

        entity.setContent(JsonUtils.toJson(Arrays.asList("title", "description", "icon"),
                Arrays.asList(title, description, icon)));
        entity.setSchedules(schedule.toJson());
        entity.setTags(JsonUtils.toJson(tags));
        entity.setTimeRange(JsonUtils.toJson(Arrays.asList("startTime", "endTime"),
                Arrays.asList(startTime, endTime)));

        return entity;
    }

    public static String getPartitionKey(HabitModel model) {
        return model.username;
    }

    public static String getRowKey(HabitModel model) {
        return model.id;
    }
}
