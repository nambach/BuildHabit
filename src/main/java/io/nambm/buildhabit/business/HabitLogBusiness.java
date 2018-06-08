package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.util.date.Day;

import java.util.List;

public interface HabitLogBusiness {

    boolean insert(HabitLogModel model);

    boolean update(HabitLogModel model);

    HabitLogModel get(String username, String habitId, int month, int year);

    List<HabitLogModel> getAllHabitLogs(String username, String habitId, Day fromMonth, Day toMonth);
}
