package io.nambm.buildhabit.model.submodel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.nambm.buildhabit.util.JsonUtils;

import java.util.List;

public class Schedule {

    public static class Repetition {
        public static final String WEEKLY = "weekly";
        public static final String MONTHLY = "monthly";
        public static final String YEARLY = "yearly";
    }

    private DailyTimePoint from;
    private DailyTimePoint to;
    private String repetition;
    private List<Object> times;

    public Schedule() {
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public List<Object> getTimes() {
        return times;
    }

    public void setTimes(List<Object> times) {
        this.times = times;
    }

    public DailyTimePoint getFrom() {
        return from;
    }

    public void setFrom(DailyTimePoint from) {
        this.from = from;
    }

    public DailyTimePoint getTo() {
        return to;
    }

    public void setTo(DailyTimePoint to) {
        this.to = to;
    }

    public static Schedule from(String json) {
        try {
            return new Gson().fromJson(json, Schedule.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}
