package io.nambm.buildhabit.model.habit;

import java.util.List;

public class DailyHabitModel {
    private String id;

    // contents
    private String title;
    private String description;
    private String timeRange;
    private String icon;

    private List<String> tags;
    private Long time;

    private boolean isShown;
    private boolean isDone = false;

    public DailyHabitModel(String id, String title, String description, String timeRange, String icon, List<String> tags, Long time, boolean isShown) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeRange = timeRange;
        this.icon = icon;
        this.tags = tags;
        this.time = time;
        this.isShown = isShown;
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

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public static DailyHabitModel from(HabitModel habitModel, long time, boolean isShown) {

        String timeRange = habitModel.getSchedule().getFrom().toString()
                + " - "
                + habitModel.getSchedule().getTo().toString();

        return new DailyHabitModel(
                habitModel.getId(),
                habitModel.getTitle(),
                habitModel.getDescription(),
                timeRange,
                habitModel.getIcon(),
                habitModel.getTags(),
                time,
                isShown
        );
    }
}
