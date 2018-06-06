package io.nambm.buildhabit.util.date;

import io.nambm.buildhabit.model.habit.Schedule;
import io.nambm.buildhabit.util.TimeUtils;

import java.util.Calendar;

public class Day {
    public final String day;
    public final int date;
    public final int month;
    public final int year;
    public final int hour;
    public final int minute;
    public final int second;
    public final long time;

    public Day(String day) {
        this.day = day;
        this.date = 0;
        this.month = 0;
        this.year = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.time = 0;
    }

    public Day(int date) {
        this.day = "";
        this.date = date;
        this.month = 0;
        this.year = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.time = 0;
    }

    public Day(int date, int month) {
        this.day = "";
        this.date = date;
        this.month = month;
        this.year = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.time = 0;
    }

    public Day(String day, int date, int month, int year, int hour, int minute, int second, long time) {
        this.day = day;
        this.date = date;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Day{" +
                "day='" + day + '\'' +
                ", date=" + date +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }

    public boolean equals(Day day, String bound) {
        if (Schedule.Repetition.WEEKLY.equals(bound)) {
            return this.day.equals(day.day);
        }

        if (Schedule.Repetition.MONTHLY.equals(bound)) {
            return this.date == day.date;
        }

        if (Schedule.Repetition.YEARLY.equals(bound)) {
            return this.date == day.date && this.month == day.month;
        }

        return false;
    }

    public static Day from(long time, int offsetMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeZone().setRawOffset(offsetMillis);
        calendar.setTimeInMillis(time);

        return TimeUtils.convert(calendar);
    }
}
