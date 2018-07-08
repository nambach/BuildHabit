package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.business.HabitGroupBusiness;
import io.nambm.buildhabit.business.HabitLogBusiness;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.habitgroup.HabitGroupModel;
import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.model.habitlog.StatisticResponse;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.util.TimeUtils;
import io.nambm.buildhabit.util.date.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HabitLogServiceImpl implements HabitLogService {

    private final HabitLogBusiness habitLogBusiness;
    private final HabitBusiness habitBusiness;
    private final HabitGroupBusiness habitGroupBusiness;

    private final HabitService habitService;

    @Autowired
    public HabitLogServiceImpl(HabitLogBusiness habitLogBusiness, HabitBusiness habitBusiness, HabitGroupBusiness habitGroupBusiness, HabitService habitService) {
        this.habitLogBusiness = habitLogBusiness;
        this.habitBusiness = habitBusiness;
        this.habitGroupBusiness = habitGroupBusiness;
        this.habitService = habitService;
    }

    @Override
    public HttpStatus addLog(String username, String habitId, long finishTime, int offsetMillis) {

        Day monthInfo = Day.from(finishTime, offsetMillis);

        HabitLogModel stub = new HabitLogModel();
        stub.setHabitId(habitId);
        stub.setUsername(username);
        stub.setMonthInfo(monthInfo);

        HabitLogModel logModel = habitLogBusiness.get(stub);

        HttpStatus result;
        if (logModel != null) {
            if (logModel.getTimes().indexOf(finishTime) == -1) {
                logModel.getTimes().add(finishTime);
            }

            result = habitLogBusiness.update(logModel, "times");
        } else {
            logModel = new HabitLogModel();
            logModel.setHabitId(habitId);
            logModel.setUsername(username);
            logModel.setMonthInfo(monthInfo);
            logModel.setTimes(Collections.singletonList(finishTime));

            result = habitLogBusiness.insert(logModel);
        }
        return result;
    }

    @Override
    public HttpStatus deleteLog(String username, String habitId, long finishTime, int offsetMillis) {
        Day monthInfo = Day.from(finishTime, offsetMillis);

        HabitLogModel stub = new HabitLogModel();
        stub.setHabitId(habitId);
        stub.setUsername(username);
        stub.setMonthInfo(monthInfo);

        HabitLogModel logModel = habitLogBusiness.get(stub);

        HttpStatus result;
        if (logModel != null) {
            logModel.getTimes().remove(finishTime);

            if (logModel.getTimes().isEmpty()) {
                habitLogBusiness.remove(logModel);
            } else {
                habitLogBusiness.update(logModel, "times");
            }

            result = HttpStatus.OK;
        } else {
            result = HttpStatus.NOT_FOUND;
        }
        return result;
    }

    @Override
    public StatisticResponse getLogs(String username, String habitId, int offsetMillis) {
        HabitModel rootHabit = habitBusiness.get(username, habitId);
        List<HabitModel> habitMembers = new LinkedList<>();
        List<StatisticResponse.StatisticEntry> logs = new LinkedList<>();

        if (rootHabit == null) {
            return null;
        }
        habitMembers.add(rootHabit);

        if (rootHabit.getGroupId() != null) {
            HabitGroupModel groupModel = habitGroupBusiness.get(rootHabit.getGroupId());

            for (String id : groupModel.getHabits()) {
                // Skip when meet rootHabit again
                if (rootHabit.getId().equals(id)) continue;

                HabitModel habit = habitBusiness.get(username, id);
                habitMembers.add(habit);
            }
        }

        Calendar calendar = TimeUtils.getCalendar(offsetMillis);

        for (HabitModel member : habitMembers) {
            long from = member.getStartTime();
            long to = member.getEndTime() != -1
                    ? member.getEndTime()
                    : System.currentTimeMillis();
            List<Long> memberLogs = habitLogBusiness.getLogsById(username, member.getId());

            for (Day day : member.getSchedule().getTimes()) {
                // Set hour and minute into day
                day.hour = member.getSchedule().getFrom().getHour();
                day.minute = member.getSchedule().getFrom().getMinute();

                List<Long> times = TimeUtils.getTimes(from, to, day, member.getSchedule().getRepetition(), calendar);
                for (Long time : times) {
                    boolean done = memberLogs.contains(time);
                    logs.add(new StatisticResponse.StatisticEntry(time, done));
                }
            }
        }

        logs.sort(Comparator.comparingLong(value -> value.time));
        habitMembers.sort((o1, o2) -> o1.getStartTime() > o2.getStartTime() ? 1 : -1);
        return new StatisticResponse(rootHabit, habitMembers, logs);
    }
}
