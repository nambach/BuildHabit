package io.nambm.buildhabit.model.habit;

import com.eclipsesource.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.nambm.buildhabit.util.JsonUtils;
import io.nambm.buildhabit.util.date.Day;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Schedule {

    public static class Repetition {
        public static final String WEEKLY = "weekly";
        public static final String MONTHLY = "monthly";
        public static final String YEARLY = "yearly";
    }

    private DailyTimePoint from;
    private DailyTimePoint to;
    private String repetition;
    private List<Day> times;
    private List<Long> reminders;

    public Schedule() {
    }

    private Schedule(DailyTimePoint from, DailyTimePoint to, String repetition, List<Day> times, List<Long> reminders) {
        this.from = from;
        this.to = to;
        this.repetition = repetition;
        this.times = times;
        this.reminders = reminders;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public List<Day> getTimes() {
        return times;
    }

    public void setTimes(List<Day> times) {
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

    public List<Long> getReminders() {
        return reminders;
    }

    public void setReminders(List<Long> reminders) {
        this.reminders = reminders;
    }

    public static Schedule from(String json) {
        try {
            Gson gson = new Gson();

            DailyTimePoint from = gson.fromJson(JsonUtils.getValue(json, "from"), DailyTimePoint.class);
            DailyTimePoint to = gson.fromJson(JsonUtils.getValue(json, "to"), DailyTimePoint.class);
            String repetition = JsonUtils.getValue(json, "repetition");
            String times = JsonUtils.getValue(json, "times");
            List<Long> reminders = JsonUtils.getArray(JsonUtils.getValue(json, "reminders"), Long.class);

            List<Day> days = null;

            if (Repetition.WEEKLY.equals(repetition)) {
                List<String> weekly = JsonUtils.getArray(times, String.class);
                days = weekly
                        .stream()
                        .map(Day::new)
                        .collect(Collectors.toList());
            } else if (Repetition.MONTHLY.equals(repetition)) {
                List<String> monthly = JsonUtils.getArray(times, String.class);
                days = monthly
                        .stream()
                        .map(s -> {
                            int date = Integer.parseInt(s);
                            return new Day(date);
                        })
                        .collect(Collectors.toList());
            } else if (Repetition.YEARLY.equals(repetition)) {
                List<String> monthly = JsonUtils.getArray(times, String.class);
                days = monthly
                        .stream()
                        .map(s -> {
                            int date = Integer.parseInt(s.substring(0, 2));
                            int month = Integer.parseInt(s.substring(3));
                            return new Day(date, month);
                        })
                        .collect(Collectors.toList());
            }

            return new Schedule(from, to, repetition, days, reminders);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("from", from);
        map.put("to", to);
        map.put("repetition", repetition);

        if (Repetition.WEEKLY.equals(repetition)) {
            map.put("times", times.stream().map(day -> day.day).collect(Collectors.toList()));
        } else if (Repetition.MONTHLY.equals(repetition)) {
            map.put("times", times.stream().map(day -> day.date + "").collect(Collectors.toList()));
        } else if (Repetition.YEARLY.equals(repetition)) {
            map.put("times", times.stream().map(day ->
                    String.format("%02d%02d", day.date, day.month)).collect(Collectors.toList()));
        }

        map.put("reminders", reminders);

        return new Gson().toJson(map);
    }
}
