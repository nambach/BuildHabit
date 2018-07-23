package io.nambm.buildhabit.model.user.follow;

import java.util.ArrayList;

public class Follower extends FollowElement {

    public Follower(String username, Long startTime) {
        super(username, startTime);
    }

    public Follower(String username) {
        super(username, System.currentTimeMillis());
    }

    public static Follower fromArray(ArrayList<String> array) {
        try {
            return new Follower(array.get(USERNAME_INDEX), Long.parseLong(array.get(FOLLOW_TIME_INDEX)));
        } catch (Exception e) {
            return null;
        }
    }
}
