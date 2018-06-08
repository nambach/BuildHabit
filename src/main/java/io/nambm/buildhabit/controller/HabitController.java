package io.nambm.buildhabit.controller;

import io.nambm.buildhabit.model.habit.DailyHabit;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(description = "Habit API")
public interface HabitController {

    @ApiOperation("Add new habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "title", value = "Title", defaultValue = ""),
            @ApiImplicitParam(name = "description", value = "Description", defaultValue = ""),
            @ApiImplicitParam(name = "schedule", value = "Schedule", defaultValue = "{\"from\":{\"hour\":5,\"minute\":0},\"to\":{\"hour\":5,\"minute\":30},\"repetition\":\"weekly\",\"times\":[\"mon\",\"tue\",\"wed\",\"thu\",\"fri\"],\"reminders\":[0]}"),
            @ApiImplicitParam(name = "tags", value = "Tags", defaultValue = "[]"),
            @ApiImplicitParam(name = "startTime", value = "Start Time", defaultValue = "0"),
            @ApiImplicitParam(name = "endTime", value = "End Time", defaultValue = "0")
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "User has been created"),
            @ApiResponse(code = 409, message = "User has already existed")
    })
    ResponseEntity add(String username, String title, String description,
                       String schedule, String tags, long startTime, long endTime);

    @ApiOperation("Check done habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "habitId", defaultValue = "", value = "Habit ID"),
            @ApiImplicitParam(name = "time", defaultValue = "0", value = "Finish time (millisecond)"),
            @ApiImplicitParam(name = "offsetMillis", defaultValue = "25200000", value = "Timezone Offset In Millisecond")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Habit log recorded"),
            @ApiResponse(code = 404, message = "Habit not found")
    })
    ResponseEntity checkDone(String username, String habitId, long time, int offsetMillis);

    @ApiOperation("Undo check done habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "habitId", defaultValue = "", value = "Habit ID"),
            @ApiImplicitParam(name = "time", defaultValue = "0", value = "Finish time (millisecond)"),
            @ApiImplicitParam(name = "offsetMillis", defaultValue = "25200000", value = "Timezone Offset In Millisecond")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Habit log recorded"),
            @ApiResponse(code = 404, message = "Habit not found")
    })
    ResponseEntity undoCheckDone(String username, String habitId, long time, int offsetMillis);

    @ApiOperation("Load all habits")
    @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username")
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<HabitModel>> getAllHabits(String username);

    @ApiOperation("Load all habits in current week")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "offsetMillis", value = "Timezone Offset In Millisecond", defaultValue = "25200000")
    })
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<DailyHabit>> getCurrentWeekHabits(String username, int offsetMillis);

    @ApiOperation("Load all habits in current week")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "from", defaultValue = "01/06/2018", value = "From date (dd/mm/yyyy)"),
            @ApiImplicitParam(name = "to", defaultValue = "30/06/2018", value = "From date (dd/mm/yyyy)"),
            @ApiImplicitParam(name = "offsetMillis", value = "Timezone Offset In Millisecond", defaultValue = "25200000")
    })
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<DailyHabit>> getHabits(String username, String from, String to, int offsetMillis);
}
