package io.nambm.buildhabit.controller;

import io.nambm.buildhabit.model.habit.HabitModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(description = "Habit API")
public interface TemplateController {

    @ApiOperation("Load templates")
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<HabitModel>> getTemplateHabits();

}
