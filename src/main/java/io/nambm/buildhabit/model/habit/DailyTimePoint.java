package io.nambm.buildhabit.model.habit;

public class DailyTimePoint {
    private int hour;
    private int minute;
    private int totalMinutes;

    public DailyTimePoint(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        this.totalMinutes = hour * 60 + minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public static DailyTimePoint from(String time) {
        StringBuilder builder = new StringBuilder(time);

        try {
            int hour = Integer.parseInt(builder.substring(0, 2));
            int minute = Integer.parseInt(builder.substring(3, 5));
            return new DailyTimePoint(hour, minute);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
