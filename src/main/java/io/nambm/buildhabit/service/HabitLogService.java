package io.nambm.buildhabit.service;

public interface HabitLogService {

    boolean addLog(String username, String habitId, long finishTime, int offsetMillis);

    boolean deleteLog(String username, String habitId, long finishTime, int offsetMillis);
}
