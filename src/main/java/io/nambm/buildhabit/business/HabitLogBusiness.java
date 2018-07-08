package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.util.date.Day;

import java.util.List;
import java.util.Map;

public interface HabitLogBusiness extends GenericBusiness<HabitLogModel> {

    List<Long> getLogsById(String username, String habitId);

    List<Long> getLogsById(String username, String habitId, Day fromMonth, Day toMonth);

    Map<String, List<Long>> getAllLogs(String username, Day from, Day to);
}
