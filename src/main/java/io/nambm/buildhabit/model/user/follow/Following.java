package io.nambm.buildhabit.model.user.follow;

public class Following extends FollowElement {

    public Following(String username, Long startTime) {
        super(username, startTime);
    }

    public Following(String username) {
        super(username, System.currentTimeMillis());
    }
}
