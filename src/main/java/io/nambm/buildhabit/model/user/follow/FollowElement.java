package io.nambm.buildhabit.model.user.follow;

import java.util.ArrayList;

public class FollowElement extends ArrayList<String> {

    public static final int USERNAME_INDEX = 0;
    public static final int FOLLOW_TIME_INDEX = 1;

    public FollowElement(String username, Long startTime) {
        add(USERNAME_INDEX, username);
        add(FOLLOW_TIME_INDEX, startTime + "");
    }

    public String getUsername() {
        return get(USERNAME_INDEX);
    }

    public void setUsername(String username) {
        add(USERNAME_INDEX, username);
    }

    public long getStartTime() {
        try {
            return Long.parseLong(get(FOLLOW_TIME_INDEX));
        } catch (Exception e) {
            return -1L;
        }
    }

    public void setStartTime(Long l) {
        if (l == null) {
            l = -1L;
        }
        add(FOLLOW_TIME_INDEX, l + "");
    }
}
