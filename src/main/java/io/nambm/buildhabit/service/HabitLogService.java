package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.habitlog.StatisticResponse;
import org.springframework.http.HttpStatus;

public interface HabitLogService {

    HttpStatus addLog(String username, String habitId, long finishTime, int offsetMillis);

    HttpStatus deleteLog(String username, String habitId, long finishTime, int offsetMillis);

    StatisticResponse getLogs(String username, String habitId, int offsetMillis);
}
