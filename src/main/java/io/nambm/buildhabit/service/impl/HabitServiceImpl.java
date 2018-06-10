package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.business.HabitLogBusiness;
import io.nambm.buildhabit.constant.AppConstant;
import io.nambm.buildhabit.model.habit.*;
import io.nambm.buildhabit.model.habitlog.HabitLogModel;
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
    private final HabitLogBusiness habitLogBusiness;

    @Autowired
    public HabitServiceImpl(HabitBusiness habitBusiness, HabitLogBusiness habitLogBusiness) {
        this.habitBusiness = habitBusiness;
        this.habitLogBusiness = habitLogBusiness;
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
        classifyHabits(habits, map, week.getDays(), Schedule.Repetition.WEEKLY, calendar);
        classifyHabits(habits, map, week.getDays(), Schedule.Repetition.MONTHLY, calendar);
        classifyHabits(habits, map, week.getDays(), Schedule.Repetition.YEARLY, calendar);

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

    @Override
    public ResponseEntity<List<DailyHabit>> getHabitsByDateRange(long from, long to, String username, String equalConditions, int offsetMillis) {
        // Init necessary variables
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeZone().setRawOffset(offsetMillis);
        List<Day> days = TimeUtils.getDays(from, to, calendar);

        // Init map to classify
        Map<Day, DailyHabit> map = new LinkedHashMap<>();
        for (Day day : days) {
            map.put(day, new DailyHabit(day, true));
        }

        List<HabitModel> habits = habitBusiness.getAllHabits(username, equalConditions);
        classifyHabits(habits, map, days, Schedule.Repetition.WEEKLY, calendar);
        classifyHabits(habits, map, days, Schedule.Repetition.MONTHLY, calendar);
        classifyHabits(habits, map, days, Schedule.Repetition.YEARLY, calendar);

        // Convert to a list
        List<DailyHabit> dailyHabits = map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        // Get habit logs
        Map<String, List<Long>> logsDict = habitLogBusiness.getAllLogs(username, days.get(0), days.get(days.size() - 1));

        // Sort habits by time
        dailyHabits.forEach(dailyHabit -> {
            dailyHabit.getHabits()
                    .sort(Comparator.comparingLong(DailyHabitModel::getTime));

            // Check done according to habit logs
            dailyHabit.getHabits().forEach(dailyHabitModel -> {
                String habitId = dailyHabitModel.getId();
                long time = dailyHabitModel.getTime();

                if (logsDict.get(habitId) != null
                        && logsDict.get(habitId).indexOf(time) != -1) {
                    dailyHabitModel.setDone(true);
                }
            });
        });


        return new ResponseEntity<>(dailyHabits, HttpStatus.OK);
    }

    private void classifyHabits(List<HabitModel> allHabits, Map<Day, DailyHabit> classifiedHabits, List<Day> days, String repetition, Calendar calendar) {

        // Get all habits and filter by 'repetition'
        List<HabitModel> weeklyHabits = allHabits
                .stream()
                .filter(habit ->
                        repetition.equals(habit.getSchedule().getRepetition()))
                .collect(Collectors.toList());

        // Prepare storage for habits that have reminder margins
        List<DailyHabitModel> unlistedHabits = new LinkedList<>();

        for (Day day : days) {
            for (HabitModel habit : weeklyHabits) {

                if (isDue(habit, day)) {
                    continue;
                }

                for (Day time : habit.getSchedule().getTimes()) {
                    if (time.equals(day, habit.getSchedule().getRepetition())) {

                        // Calculate the time to alarm
                        long timeToAlarm = getAlarmTimeMillis(day, habit.getSchedule().getFrom(), calendar);

                        classifiedHabits.get(day).getHabits()
                                .add(DailyHabitModel.from(habit, timeToAlarm, true));

                        // Add the unlisted habits
                        for (Long reminderMargin : habit.getSchedule().getReminders()) {
                            if (reminderMargin <= 0) continue;
                            unlistedHabits.add(DailyHabitModel.from(habit, timeToAlarm - reminderMargin, false));
                        }
                    }
                }
            }
        }

        // RE-classify the habits with reminder margins
        for (Day day : days) {
            for (DailyHabitModel habit : unlistedHabits) {
                if (day.include(habit.getTime(), calendar)) {
                    classifiedHabits.get(day).getHabits().add(habit);
                }
            }
        }
    }

    private long getAlarmTimeMillis(Day day, DailyTimePoint timePoint, Calendar calendar) {
        return TimeUtils.combineTimeMillis(day.time, timePoint.getHour(), timePoint.getMinute(), calendar);
    }

    /**
     * Calculate if habit id due in the day
     *
     * @param habit
     * @param day the day on which habit occurs
     * @return
     */
    private boolean isDue(HabitModel habit, Day day) {
        // if 'endTime' = -1 then ignore endTime
        return day.time + AppConstant.DAY_IN_MILLISECOND - 1 < habit.getStartTime()
                || (habit.getEndTime() != -1 && habit.getEndTime() < day.time);
    }
}
