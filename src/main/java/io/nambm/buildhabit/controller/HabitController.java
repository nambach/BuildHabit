package io.nambm.buildhabit.controller;

import io.nambm.buildhabit.model.habit.DailyHabit;
import io.nambm.buildhabit.model.habit.DailyHabitModel;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(description = "Habit API")
public interface HabitController {

    ResponseEntity<String> addV2(String body);
    ResponseEntity<String> checkDoneV2(String body);
    ResponseEntity<String> undoCheckDone(String body);

    @ApiOperation("Get habit by ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "Username", defaultValue = "nambm"),
            @ApiImplicitParam(name = "habitId", value = "Habit ID", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Habit does not exist")
    })
    ResponseEntity<HabitModel> get(String username, String habitId);

    @ApiOperation("Load all habits")
    @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username")
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<HabitModel>> getAllHabits(String username);

    @ApiOperation("Load all habits by time range")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "from", defaultValue = "06/01/2018", value = "From date (mm/dd/yyyy)"),
            @ApiImplicitParam(name = "to", defaultValue = "06/30/2018", value = "From date (mm/dd/yyyy)"),
            @ApiImplicitParam(name = "offsetMillis", value = "Timezone Offset In Millisecond", defaultValue = "25200000")
    })
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<DailyHabit>> getHabits(String username, String from, String to, int offsetMillis);

    @ApiOperation("Load all habits by date offset")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "mode", defaultValue = "future", value = "Mode (\"future\" or \"past\")"),
            @ApiImplicitParam(name = "dateOffset", defaultValue = "2", value = "Number of date (offset)"),
            @ApiImplicitParam(name = "offsetMillis", value = "Timezone Offset In Millisecond", defaultValue = "25200000")
    })
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<DailyHabitModel>> getHabitsByDateOffset(String username, String mode, int dateOffset, int offsetMillis);
}
