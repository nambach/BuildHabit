package io.nambm.buildhabit.model.user;

import com.google.gson.Gson;
import io.nambm.buildhabit.entity.FollowingEntity;
import io.nambm.buildhabit.model.GenericModel;

import java.util.List;

public class FollowingModel extends GenericModel<FollowingEntity> {

    private String username;
    private List<Follower> followers;
    private List<Following> followings;

    public FollowingModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Following> getFollowings() {
        return followings;
    }

    public void setFollowings(List<Following> followings) {
        this.followings = followings;
    }

    @Override
    public String getPartitionKey() {
        return username;
    }

    @Override
    public String getRowKey() {
        return username;
    }

    @Override
    public FollowingEntity toEntity() {
        Gson gson = new Gson();

        FollowingEntity entity = new FollowingEntity();

        entity.setPartitionKey(getPartitionKey());
        entity.setRowKey(getRowKey());

        entity.setUsername(username);
        entity.setFollowers(gson.toJson(followers));
        entity.setFollowings(gson.toJson(followings));

        return entity;
    }
}
