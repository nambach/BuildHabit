package io.nambm.buildhabit.model.user;

import java.util.ArrayList;

public class Follower extends ArrayList<String> {

    public Follower(String username) {
        add(0, username);
    }

    public String getUsername() {
        return get(0);
    }

    public void setUsername(String username) {
        add(0, username);
    }
}
