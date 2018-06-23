package io.nambm.buildhabit.util.date;

import io.nambm.buildhabit.model.habit.Schedule;
import io.nambm.buildhabit.util.TimeUtils;

import java.util.Calendar;

public class Day {
    public String day;
    public int date;
    public int month;
    public int year;
    public int hour;
    public int minute;
    public int second;
    public long time;

    public Day() {
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

    public boolean include(long time, Calendar calendar) {
        calendar.setTimeInMillis(this.time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        calendar.setTimeInMillis(this.time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();

        return startTime <= time && time <= endTime;
    }

    public static Day from(long time, int offsetMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeZone().setRawOffset(offsetMillis);
        calendar.setTimeInMillis(time);

        return TimeUtils.convert(calendar);
    }
}
