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
        List<HabitModel> habits = habitBusiness.getAllHabits(username, equalConditions);

        Week week = TimeUtils.getCurrentWeek(offsetMillis);

        // Init map to classify
        Map<String, DailyHabit> map = new LinkedHashMap<>();
        for (Day day : week) {
            map.put(day.day, new DailyHabit(day, true));
        }

        // Classify habits according to dayOfWeek
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeZone().setRawOffset(offsetMillis);
        for (HabitModel habit : habits) {
            String repetition = habit.getSchedule().getRepetition();
            if (Schedule.Repetition.WEEKLY.equals(repetition)) {

                for (Object time : habit.getSchedule().getTimes()) {
                    String dayOfWeek = (String) time;
                    DailyHabit dailyHabit = map.get(dayOfWeek);
                    dailyHabit.getHabits().add(DailyHabitModel.from(habit, getAlarmTimeMillis(dailyHabit.getDay(), habit, calendar)));
                }
            }
        }

        // Convert to a list
        List<DailyHabit> dailyHabits = map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        // Sort habits by time
        dailyHabits.forEach(dailyHabit ->
                dailyHabit.getHabits().sort(Comparator.comparingLong(DailyHabitModel::getTime))
        );

        return new ResponseEntity<>(dailyHabits, HttpStatus.OK);
    }

    private long getAlarmTimeMillis(Day day, HabitModel habitModel, Calendar calendar) {
        DailyTimePoint from = habitModel.getSchedule().getFrom();
        return TimeUtils.combineTimeMillis(day.time, from.getHour(), from.getMinute(), calendar);
    }
}
