package io.nambm.buildhabit.model.user;

import com.google.gson.Gson;
import io.nambm.buildhabit.entity.FollowEntity;
import io.nambm.buildhabit.model.GenericModel;
import io.nambm.buildhabit.model.user.follow.Follower;
import io.nambm.buildhabit.model.user.follow.Followers;
import io.nambm.buildhabit.model.user.follow.Following;
import io.nambm.buildhabit.model.user.follow.Followings;

public class FollowModel extends GenericModel<FollowEntity> {

    private String username;
    private Followers followers;
    private Followings followings;

    public FollowModel() {
        followers = new Followers();
        followings = new Followings();
    }

    public void addFollower(UserModel follower) {
        boolean checkExist = followers.stream().anyMatch(flw -> flw.getUsername().equals(follower.getUsername()));
        if (checkExist) {
            return;
        }

        followers.add(new Follower(follower.getUsername()));
    }

    public void removeFollower(UserModel follower) {
        Follower toRemove = null;
        for (Follower flw : followers) {
            if (flw.getUsername().equals(follower.getUsername())) {
                toRemove = flw;
                break;
            }
        }

        if (toRemove != null) {
            followers.remove(toRemove);
        }
    }

    public void follow(UserModel target) {
        boolean checkExist = followings.stream().anyMatch(flw -> flw.getUsername().equals(target.getUsername()));
        if (checkExist) {
            return;
        }

        followings.add(new Following(target.getUsername()));
    }

    public void unfollow(UserModel target) {
        Following toRemove = null;
        for (Following flw : followings) {
            if (flw.getUsername().equals(target.getUsername())) {
                toRemove = flw;
                break;
            }
        }

        if (toRemove != null) {
            followings.remove(toRemove);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public Followings getFollowings() {
        return followings;
    }

    public void setFollowings(Followings followings) {
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
    public FollowEntity toEntity() {
        Gson gson = new Gson();

        FollowEntity entity = new FollowEntity();

        entity.setPartitionKey(getPartitionKey());
        entity.setRowKey(getRowKey());

        entity.setUsername(username);
        entity.setFollowers(gson.toJson(followers));
        entity.setFollowings(gson.toJson(followings));

        return entity;
    }
}
