package io.nambm.buildhabit.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.nambm.buildhabit.model.user.follow.Follower;
import io.nambm.buildhabit.model.user.follow.Following;
import io.nambm.buildhabit.model.user.FollowModel;

import java.util.List;

public class FollowEntity extends GenericEntity<FollowModel> {

    private String username;
    private String followers;
    private String followings;

    public FollowEntity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowings() {
        return followings;
    }

    public void setFollowings(String followings) {
        this.followings = followings;
    }

    @Override
    public FollowModel toModel() {
        Gson gson = new Gson();

        FollowModel model = new FollowModel();

        model.setUsername(username);
        model.setFollowers(gson.fromJson(followers, new TypeToken<List<Follower>>(){}.getType()));
        model.setFollowings(gson.fromJson(followings, new TypeToken<List<Following>>(){}.getType()));

        return model;
    }
}
