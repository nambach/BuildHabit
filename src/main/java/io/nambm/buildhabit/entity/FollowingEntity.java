package io.nambm.buildhabit.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.nambm.buildhabit.model.user.Follower;
import io.nambm.buildhabit.model.user.FollowingModel;

import java.util.List;

public class FollowingEntity extends GenericEntity<FollowingModel> {

    private String username;
    private String followers;
    private String followings;

    public FollowingEntity() {
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
    public FollowingModel toModel() {
        Gson gson = new Gson();

        FollowingModel model = new FollowingModel();

        model.setUsername(username);
        model.setFollowers(gson.fromJson(followers, new TypeToken<List<Follower>>(){}.getType()));
        return null;
    }
}
