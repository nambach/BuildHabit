package io.nambm.buildhabit.model.user.follow;

import java.util.ArrayList;

public class FollowElement extends ArrayList<String> {

    public FollowElement(String username, Long startTime) {
        add(0, username);
        add(1, startTime + "");
    }

    public String getUsername() {
        return get(0);
    }

    public void setUsername(String username) {
        add(0, username);
    }

    public long getStartTime() {
        try {
            return Long.parseLong(get(1));
        } catch (Exception e) {
            return -1L;
        }
    }

    public void setStartTime(Long l) {
        if (l == null) {
            l = -1L;
        }
        add(1, l + "");
    }
}
