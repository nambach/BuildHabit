package io.nambm.buildhabit.model.user.follow;

import java.util.ArrayList;

public class Following extends FollowElement {

    public Following(String username, Long startTime) {
        super(username, startTime);
    }

    public Following(String username) {
        super(username, System.currentTimeMillis());
    }

    public static Following fromArray(ArrayList<String> array) {
        try {
            return new Following(array.get(USERNAME_INDEX), Long.parseLong(array.get(FOLLOW_TIME_INDEX)));
        } catch (Exception e) {
            return null;
        }
    }
}
