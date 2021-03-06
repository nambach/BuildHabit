package io.nambm.buildhabit.util;

import io.nambm.buildhabit.constant.AppConstant;
import io.nambm.buildhabit.model.habit.Schedule;
import io.nambm.buildhabit.util.date.Day;
import io.nambm.buildhabit.util.date.Week;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtils {

    public static final List<String> DAY_OF_WEEK = Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun");
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String MM_DD_YYYY = "MM/dd/yyyy";

    public static final String FUTURE = "future";
    public static final String PAST = "past";

    public static Calendar getCalendar(int offsetMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeZone().setRawOffset(offsetMillis);
        return calendar;
    }

    public static int getCalendarDayOfWeek(String dayOfWeek) {
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

    public static String getCalendarDayOfWeek(int dayOfWeek) {
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

    public static Day convert(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE");
        dateFormat.setTimeZone(new SimpleTimeZone(calendar.getTimeZone().getRawOffset(), "ID"));
        String dayOfWeek = dateFormat.format(calendar.getTime());
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

    public static List<Day> getDays(long from, long to, Calendar calendar) {
        List<Day> days = new LinkedList<>();

        // Set the 'to' date
        calendar.setTimeInMillis(to);
        calendar.set(Calendar.HOUR_OF_DAY, 23); // ! clear would not reset the hour of day !
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long boundary = calendar.getTimeInMillis();

        // Set the 'from' date
        calendar.setTimeInMillis(from);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        while (calendar.getTimeInMillis() < boundary) {
            days.add(convert(calendar));
            calendar.add(Calendar.DATE, 1);
        }

        return days;
    }

    public static long combineTimeMillis(long timeMillis, int hour, int minute, Calendar calendar) {
        calendar.setTimeInMillis(timeMillis);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

    public static long getTimeMillis(String time, String timeUtilsPattern, int offsetMillis) {
        if (DD_MM_YYYY.equals(timeUtilsPattern)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeUtilsPattern);
            dateFormat.setTimeZone(new SimpleTimeZone(offsetMillis, "ID"));
            try {
                return dateFormat.parse(time).getTime();
            } catch (ParseException ignored) {
            }
        }

        if (MM_DD_YYYY.equals(timeUtilsPattern)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeUtilsPattern);
            dateFormat.setTimeZone(new SimpleTimeZone(offsetMillis, "ID"));
            try {
                return dateFormat.parse(time).getTime();
            } catch (ParseException ignored) {
            }
        }

        return 0;
    }

    public static long getByDateOffset(long current, String mode, int offset, Calendar calendar) {
        calendar.setTimeInMillis(current);

        switch (mode) {
            case FUTURE:
                calendar.add(Calendar.DATE, offset);
                break;
            case PAST:
                calendar.add(Calendar.DATE, -1 * offset);
                break;
        }

        return calendar.getTimeInMillis();
    }

    public static List<Long> getTimes(long from, long to, Day time, String repetition, Calendar calendar) {
        List<Long> times = new LinkedList<>();

        // Set the 'to' date
        calendar.setTimeInMillis(to);
        calendar.set(Calendar.HOUR_OF_DAY, 23); // ! clear would not reset the hour of day !
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long boundary = calendar.getTimeInMillis();

        // Set the 'from' date
        calendar.setTimeInMillis(from);
        calendar.set(Calendar.HOUR_OF_DAY, time.hour); // ! clear would not reset the hour of day !
        calendar.set(Calendar.MINUTE, time.minute);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        switch (repetition) {
            case Schedule.Repetition.WEEKLY:
                calendar.set(Calendar.DAY_OF_WEEK, getCalendarDayOfWeek(time.day));
                while (calendar.getTimeInMillis() < from) {
                    calendar.add(Calendar.DATE, 7);
                }
                break;
            case Schedule.Repetition.MONTHLY:
                calendar.set(Calendar.DATE, time.date);
                while (calendar.getTimeInMillis() < from) {
                    calendar.add(Calendar.MONTH, 1);
                }
                break;
            case Schedule.Repetition.YEARLY:
                calendar.set(Calendar.DATE, time.date);
                calendar.set(Calendar.MONTH, time.month);
                while (calendar.getTimeInMillis() < from) {
                    calendar.add(Calendar.YEAR, 1);
                }
                break;
        }

        while (calendar.getTimeInMillis() < boundary) {
            times.add(calendar.getTimeInMillis());

            switch (repetition) {
                case Schedule.Repetition.WEEKLY:
                    calendar.add(Calendar.DATE, 7);
                    break;
                case Schedule.Repetition.MONTHLY:
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case Schedule.Repetition.YEARLY:
                    calendar.add(Calendar.YEAR, 1);
                    break;
            }
        }

        return times;
    }
}
