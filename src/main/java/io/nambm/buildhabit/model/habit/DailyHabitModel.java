package io.nambm.buildhabit.model.habit;

import java.util.List;

public class DailyHabitModel {
    private String id;

    // contents
    private String title;
    private String description;
    private String icon;

    private List<String> tags;
    private Long time;

    //TODO: margin times, from - to

    public DailyHabitModel(String id, String title, String description, String icon, List<String> tags, Long time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.tags = tags;
        this.time = time;
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

    public static DailyHabitModel from(HabitModel habitModel, long time) {

        return new DailyHabitModel(
                habitModel.getId(),
                habitModel.getTitle(),
                habitModel.getDescription(),
                habitModel.getIcon(),
                habitModel.getTags(),
                time
        );
    }
}
