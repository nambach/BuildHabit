package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.model.habit.*;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.util.TimeUtils;
import io.nambm.buildhabit.util.date.Day;
import io.nambm.buildhabit.util.date.Week;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HabitServiceImpl implements HabitService {

    private final HabitBusiness habitBusiness;

    @Autowired
    public HabitServiceImpl(HabitBusiness habitBusiness) {
        this.habitBusiness = habitBusiness;
    }

    @Override
    public HttpStatus insert(HabitModel model) {
        return habitBusiness.insert(model) ? HttpStatus.CREATED : HttpStatus.CONFLICT;
    }

    @Override
    public HttpStatus update(HabitModel model) {
        return null;
    }

    @Override
    public HttpStatus remove(HabitModel model) {
        return null;
    }

    @Override
    public ResponseEntity<List<HabitModel>> getAllHabits(String username, String equalConditions) {
        return new ResponseEntity<>(habitBusiness.getAllHabits(username, equalConditions), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DailyHabit>> getThisWeekHabits(String username, String equalConditions, int offsetMillis) {
        // Init necessary variables
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeZone().setRawOffset(offsetMillis);
        Week week = TimeUtils.getCurrentWeek(offsetMillis);

        // Init map to classify
        Map<Day, DailyHabit> map = new LinkedHashMap<>();
        for (Day day : week) {
            map.put(day, new DailyHabit(day, true));
        }

        List<HabitModel> habits = habitBusiness.getAllHabits(username, equalConditions);
        classifyWeeklyHabits(calendar, week, map, habits);
        classifyMonthlyHabits(calendar, week, map, habits);
        classifyYearlyHabits(calendar, week, map, habits);

        // Convert to a list
        List<DailyHabit> dailyHabits = map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        // Sort habits by time
        dailyHabits.forEach(dailyHabit ->
                dailyHabit.getHabits()
                        .sort(Comparator.comparingLong(DailyHabitModel::getTime))
        );

        return new ResponseEntity<>(dailyHabits, HttpStatus.OK);
    }

    private void classifyWeeklyHabits(Calendar calendar, Week week, Map<Day, DailyHabit> classifiedHabits, List<HabitModel> allHabits) {

        // Get weekly habits
        List<HabitModel> weeklyHabits = allHabits
                .stream()
                .filter(habit ->
                        Schedule.Repetition.WEEKLY.equals(habit.getSchedule().getRepetition()))
                .collect(Collectors.toList());

        // Classify habits according to day of week
        weeklyHabits.forEach(weeklyHabit -> {

            // Iterate all days of week
            List<Day> daysOfWeek = weeklyHabit.getSchedule().getTimes();
            daysOfWeek.forEach(dayOfWeek -> {

                DailyHabit dailyHabit = classifiedHabits.get(week.get(dayOfWeek.day));

                // Calculate the time to alarm
                long timeToAlarm = getAlarmTimeMillis(dailyHabit.getDay(), weeklyHabit.getSchedule().getFrom(), calendar);

                dailyHabit.getHabits().add(
                        DailyHabitModel.from(weeklyHabit, timeToAlarm)
                );
            });
        });
    }

    private void classifyMonthlyHabits(Calendar calendar, Week week, Map<Day, DailyHabit> classifiedHabits, List<HabitModel> allHabits) {

        // Get monthly habits
        List<HabitModel> monthlyHabits = allHabits
                .stream()
                .filter(habit ->
                        Schedule.Repetition.MONTHLY.equals(habit.getSchedule().getRepetition()))
                .collect(Collectors.toList());

        // Classify habits according to day of month
        for (Day day : week) {
            for (HabitModel habit : monthlyHabits) {
                for (Day time : habit.getSchedule().getTimes()) {
                    if (time.equals(day, habit.getSchedule().getRepetition())) {

                        // Calculate the time to alarm
                        long timeToAlarm = getAlarmTimeMillis(day, habit.getSchedule().getFrom(), calendar);

                        classifiedHabits.get(day).getHabits()
                                .add(DailyHabitModel.from(habit, timeToAlarm));
                    }
                }
            }
        }

    }

    private void classifyYearlyHabits(Calendar calendar, Week week, Map<Day, DailyHabit> classifiedHabits, List<HabitModel> allHabits) {

        // Get monthly habits
        List<HabitModel> monthlyHabits = allHabits
                .stream()
                .filter(habit ->
                        Schedule.Repetition.YEARLY.equals(habit.getSchedule().getRepetition()))
                .collect(Collectors.toList());

        // Classify habits according to day of month
        for (Day day : week) {
            for (HabitModel habit : monthlyHabits) {
                for (Day time : habit.getSchedule().getTimes()) {
                    if (time.equals(day, habit.getSchedule().getRepetition())) {

                        // Calculate the time to alarm
                        long timeToAlarm = getAlarmTimeMillis(day, habit.getSchedule().getFrom(), calendar);

                        classifiedHabits.get(day).getHabits()
                                .add(DailyHabitModel.from(habit, timeToAlarm));
                    }
                }
            }
        }

    }

    private long getAlarmTimeMillis(Day day, DailyTimePoint timePoint, Calendar calendar) {
        // Todo: Apply the reminder margin
        return TimeUtils.combineTimeMillis(day.time, timePoint.getHour(), timePoint.getMinute(), calendar);
    }

    /**
     * Compare time and timePoint
     *
     * @param time in millisecond
     * @param timePoint in {@link Day}
     * @param calendar provided Calendar
     * @param repetition type of the timePoint
     * @return
     */
    private boolean compare(long time, Day timePoint, Calendar calendar, String repetition) {
        calendar.setTimeInMillis(time);

        if (Schedule.Repetition.MONTHLY.equals(repetition)) {
            return calendar.get(Calendar.DATE) == timePoint.date;
        }

        if (Schedule.Repetition.YEARLY.equals(repetition)) {
            return calendar.get(Calendar.DATE) == timePoint.date
                    && calendar.get(Calendar.MONTH) + 1 == timePoint.month;
        }

        return false;
    }
}
