package io.nambm.buildhabit.entity;

import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.habit.Schedule;
import io.nambm.buildhabit.table.annotation.AzureTableName;
import io.nambm.buildhabit.util.JsonUtils;

@AzureTableName("habit")
public class HabitEntity extends GenericEntity<HabitModel> {
    private String content;
    private String schedules;
    private String tags;
    private String timeRange;
    private String groupId;
    private String privateMode;

    public HabitEntity() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSchedules() {
        return schedules;
    }

    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
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

    public HabitModel toModel() {
        HabitModel model = new HabitModel();

        model.setUsername(getPartitionKey());
        model.setId(getRowKey());

        model.setTitle(JsonUtils.getValue(content, "title", String.class));
        model.setDescription(JsonUtils.getValue(content, "description", String.class));
        model.setIcon(JsonUtils.getValue(content, "icon", String.class));

        model.setSchedule(Schedule.from(schedules));
        model.setTags(JsonUtils.getArray(tags, String.class));

        model.setStartTime(JsonUtils.getValue(timeRange, "startTime", Long.class));
        model.setEndTime(JsonUtils.getValue(timeRange, "endTime", Long.class));

        model.setGroupId(this.groupId);
        model.setPrivateMode(this.privateMode);

        return model;
    }
}
