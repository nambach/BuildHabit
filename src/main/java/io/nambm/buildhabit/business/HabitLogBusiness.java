package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.util.date.Day;

import java.util.List;
import java.util.Map;

public interface HabitLogBusiness {

    boolean insert(HabitLogModel model);

    boolean update(HabitLogModel model);

    boolean remove(HabitLogModel model);

    HabitLogModel get(String username, String habitId, int month, int year);

    List<Long> getLogsById(String username, String habitId);

    List<HabitLogModel> getLogsById(String username, String habitId, Day fromMonth, Day toMonth);

    Map<String, List<Long>> getAllLogs(String username, Day from, Day to);
}
