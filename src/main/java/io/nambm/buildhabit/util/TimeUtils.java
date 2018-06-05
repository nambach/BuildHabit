package io.nambm.buildhabit.util;

import io.nambm.buildhabit.constant.AppConstant;
import io.nambm.buildhabit.util.date.Day;
import io.nambm.buildhabit.util.date.Week;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {

    public static final List<String> DAY_OF_WEEK = Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun");

    private static int getCalendarDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek.toLowerCase()) {
            case AppConstant.MON:
                return Calendar.MONDAY;
            case AppConstant.TUE:
                return Calendar.TUESDAY;
            case AppConstant.WED:
                return Calendar.WEDNESDAY;
            case AppConstant.THU:
                return Calendar.THURSDAY;
            case AppConstant.FRI:
                return Calendar.FRIDAY;
            case AppConstant.SAT:
                return Calendar.SATURDAY;
            case AppConstant.SUN:
                return Calendar.SUNDAY;
            default:
                return Calendar.MONDAY;
        }
    }

    private static String getCalendarDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return AppConstant.MON;
            case Calendar.TUESDAY:
                return AppConstant.TUE;
            case Calendar.WEDNESDAY:
                return AppConstant.WED;
            case Calendar.THURSDAY:
                return AppConstant.THU;
            case Calendar.FRIDAY:
                return AppConstant.FRI;
            case Calendar.SATURDAY:
                return AppConstant.SAT;
            case Calendar.SUNDAY:
                return AppConstant.SUN;
            default:
                return AppConstant.MON;
        }
    }

    private static Day convert(Calendar calendar) {
        String dayOfWeek = new SimpleDateFormat("EE").format(calendar.getTime());
        return new Day(dayOfWeek.toLowerCase(),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,   // Calendar.MONTH count from 0
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.getTimeInMillis());
    }

    public static Week getCurrentWeek(Integer offsetMillis) {
        Calendar calendar = Calendar.getInstance();
        if (offsetMillis != null) {
            calendar.getTimeZone().setRawOffset(offsetMillis);
        }

        // get today and clear time of day
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Day monday = convert(calendar);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        Day tuesday = convert(calendar);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        Day wednesday = convert(calendar);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        Day thursday = convert(calendar);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Day friday = convert(calendar);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Day saturday = convert(calendar);

        // get end of this week in milliseconds
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Day sunday = convert(calendar);

        return new Week(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    public static long combineTimeMillis(long timeMillis, int hour, int minute, Calendar calendar) {
        calendar.setTimeInMillis(timeMillis);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }
}
