package io.nambm.buildhabit.util.date;

public class Day {
    public final String day;
    public final int date;
    public final int month;
    public final int year;
    public final int hour;
    public final int minute;
    public final int second;
    public final long time;


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
}
