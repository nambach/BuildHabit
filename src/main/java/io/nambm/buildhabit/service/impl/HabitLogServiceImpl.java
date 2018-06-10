package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitLogBusiness;
import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.util.date.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class HabitLogServiceImpl implements HabitLogService {

    private final HabitLogBusiness habitLogBusiness;

    @Autowired
    public HabitLogServiceImpl(HabitLogBusiness habitLogBusiness) {
        this.habitLogBusiness = habitLogBusiness;
    }

    @Override
    public boolean addLog(String username, String habitId, long finishTime, int offsetMillis) {

        Day day = Day.from(finishTime, offsetMillis);
        HabitLogModel logModel = habitLogBusiness.get(username, habitId, day.month, day.year);

        boolean result;
        if (logModel != null) {
            if (logModel.getTimes().indexOf(finishTime) == -1) {
                logModel.getTimes().add(finishTime);
            }

            result = habitLogBusiness.update(logModel);
        } else {
            logModel = new HabitLogModel();
            logModel.setHabitId(habitId);
            logModel.setUsername(username);
            logModel.setMonthInfo(day);
            logModel.setTimes(Collections.singletonList(finishTime));

            result = habitLogBusiness.insert(logModel);
        }
        return result;
    }

    @Override
    public boolean deleteLog(String username, String habitId, long finishTime, int offsetMillis) {
        Day day = Day.from(finishTime, offsetMillis);
        HabitLogModel logModel = habitLogBusiness.get(username, habitId, day.month, day.year);

        boolean result;
        if (logModel != null) {
            logModel.getTimes().remove(finishTime);

            if (logModel.getTimes().isEmpty()) {
                habitLogBusiness.remove(logModel);
                result = true;
            } else {
                result = habitLogBusiness.update(logModel);
            }
        } else {
            result = false;
        }
        return result;
    }
}
