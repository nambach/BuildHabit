package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.habit.HabitModel;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface TagService {

    HttpStatus importTagsFrom(HabitModel habitModel);

    HttpStatus addTagsToHabit(String username, String habitId, List<String> tagNames);

    HttpStatus removeTagsFromHabit(String username, String habitId, List<String> tagNames);
}
