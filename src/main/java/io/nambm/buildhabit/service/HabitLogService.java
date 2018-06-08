package io.nambm.buildhabit.service;

public interface HabitLogService {

    boolean addLog(String username, String habitId, long finishTime, int offsetMillis);
}
