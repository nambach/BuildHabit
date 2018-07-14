package io.nambm.buildhabit.entity;

import io.nambm.buildhabit.model.tag.TagModel;
import io.nambm.buildhabit.table.annotation.AzureTableName;
import io.nambm.buildhabit.util.JsonUtils;

@AzureTableName("habittag")
public class TagEntity extends GenericEntity<TagModel> {

    private String tagName;
    private String status;
    private String habitId;
    private String username;
    private String privateMode;

    public TagEntity() {
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPrivateMode() {
        return privateMode;
    }

    public void setPrivateMode(String privateMode) {
        this.privateMode = privateMode;
    }

    @Override
    public TagModel toModel() {
        TagModel model = new TagModel();

        model.setTagName(this.tagName);
        model.setStatus(this.status);
        model.setHabitId(this.habitId);
        model.setUsername(this.username);
        model.setPrivateMode(this.privateMode);

        return model;
    }
}
