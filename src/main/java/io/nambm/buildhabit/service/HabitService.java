package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.HabitModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HabitService {
    HttpStatus insert(HabitModel model);

    HttpStatus update(HabitModel model);

    HttpStatus remove(HabitModel model);

    ResponseEntity<List<HabitModel>> getAllHabits(String username, String equalConditions);
}
