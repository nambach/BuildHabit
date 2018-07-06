package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.habitlog.StatisticResponse;

public interface HabitLogService {

    boolean addLog(String username, String habitId, long finishTime, int offsetMillis);

    boolean deleteLog(String username, String habitId, long finishTime, int offsetMillis);

    StatisticResponse getLogs(String username, String habitId, int offsetMillis);
}
