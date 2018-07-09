package io.nambm.buildhabit.model.tag;

import io.nambm.buildhabit.entity.TagEntity;
import io.nambm.buildhabit.model.GenericModel;

public class TagModel extends GenericModel<TagEntity> {

    public static class Status {
        public static final String VERIFIED = "verified";
        public static final String PENDING = "pending";
        public static final String PERSONAL_USED = "personalUsed";
    }

    private String tagName;
    private String status;
    private String habitId;
    private String username;
    private String privateMode;

    public TagModel() {
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
    public String getPartitionKey() {
        return tagName;
    }

    @Override
    public String getRowKey() {
        return habitId;
    }

    @Override
    public TagEntity toEntity() {
        TagEntity entity = new TagEntity();

        entity.setPartitionKey(this.getPartitionKey());
        entity.setRowKey(this.getRowKey());

        entity.setTagName(this.tagName);
        entity.setStatus(this.status);
        entity.setHabitId(this.habitId);
        entity.setUsername(this.username);
        entity.setPrivateMode(this.privateMode);

        return entity;
    }
}
