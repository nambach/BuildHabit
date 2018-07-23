package io.nambm.buildhabit.model.user.follow;

public class Follower extends FollowElement {

    public Follower(String username, Long startTime) {
        super(username, startTime);
    }

    public Follower(String username) {
        super(username, System.currentTimeMillis());
    }
}
