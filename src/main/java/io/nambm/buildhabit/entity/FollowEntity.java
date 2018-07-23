package io.nambm.buildhabit.entity;

import com.google.gson.Gson;
import io.nambm.buildhabit.model.user.FollowModel;
import io.nambm.buildhabit.model.user.follow.Follower;
import io.nambm.buildhabit.model.user.follow.Followers;
import io.nambm.buildhabit.model.user.follow.Following;
import io.nambm.buildhabit.model.user.follow.Followings;
import io.nambm.buildhabit.table.annotation.AzureTableName;

import java.util.ArrayList;

@AzureTableName("userfollow")
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
        Followers followers = gson.fromJson(this.followers, Followers.class);
        Followings followings = gson.fromJson(this.followings, Followings.class);

        model.setFollowers(convert(followers));
        model.setFollowings(convert(followings));

        return model;
    }

    /**
     * For the Gson cannot cast from Array<String> directly into Following, hence we must cast it indirectly
     * @param flws followings
     * @return iterate-able followings
     */
    private static Followings convert(Followings flws) {
        Followings followings = new Followings();
        for (ArrayList<String> arr : flws) {
            Following following = Following.fromArray(arr);

            if (following == null) {
                continue;
            }

            followings.add(following);
        }

        return followings;
    }

    /**
     * For the Gson cannot cast from Array<String> directly into Following, hence we must cast it indirectly
     * @param flws followers
     * @return iterate-able followers
     */
    private static Followers convert(Followers flws) {
        Followers followers = new Followers();
        for (ArrayList<String> arr : flws) {
            Follower follower = Follower.fromArray(arr);

            if (follower == null) {
                continue;
            }

            followers.add(follower);
        }

        return followers;
    }
}
