package io.nambm.buildhabit.model.habit;

import io.nambm.buildhabit.entity.HabitEntity;
import io.nambm.buildhabit.model.GenericModel;
import io.nambm.buildhabit.util.JsonUtils;

import java.util.Arrays;
import java.util.List;

public class HabitModel implements GenericModel<HabitEntity> {

    public static class PRIVATE_MODE {
        public static final String PUBLIC = "public";
        public static final String PRIVATE = "private";
        public static final String PROTECTED = "protected";
    }

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

    private String groupId;
    private String privateMode;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPrivateMode() {
        return privateMode;
    }

    public void setPrivateMode(String privateMode) {
        this.privateMode = privateMode;
    }

    public HabitEntity toEntity() {
        HabitEntity entity = new HabitEntity();

        entity.setPartitionKey(this.getPartitionKey());
        entity.setRowKey(this.getRowKey());

        entity.setContent(JsonUtils.toJson(Arrays.asList("title", "description", "icon"),
                Arrays.asList(title, description, icon)));
        entity.setSchedules(schedule.toJson());
        entity.setTags(JsonUtils.toJson(tags));
        entity.setTimeRange(JsonUtils.toJson(Arrays.asList("startTime", "endTime"),
                Arrays.asList(startTime, endTime)));

        entity.setGroupId(this.groupId);
        entity.setPrivateMode(this.privateMode);

        return entity;
    }

    public String getPartitionKey() {
        return this.username;
    }

    public String getRowKey() {
        return this.id;
    }

    public static HabitModel parseRequest(String body) {
        HabitModel habitModel = new HabitModel();

        String username = JsonUtils.getValue(body, "username");
        String id = JsonUtils.getValue(body, "id");
        String title = JsonUtils.getValue(body, "title");
        String description = JsonUtils.getValue(body, "description");
        String icon = JsonUtils.getValue(body, "icon");
        String schedule = JsonUtils.getValue(body, "schedule");
        String tags = JsonUtils.getValue(body, "tags");

        habitModel.setUsername(username);
        habitModel.setId(id);

        habitModel.setTitle(title);
        habitModel.setDescription(description);
        habitModel.setIcon(icon);

        habitModel.setSchedule(Schedule.from(schedule));
        habitModel.setTags(JsonUtils.getArray(tags, String.class));

        return habitModel;
    }

    public String generateId() {
        if (this.username == null) {
            this.username = "";
        }

        return this.username + "_" + System.currentTimeMillis();
    }
}
