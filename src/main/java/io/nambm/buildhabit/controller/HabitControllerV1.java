package io.nambm.buildhabit.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

@Api(description = "Habit API (v1)")
public interface HabitControllerV1 {

    @ApiOperation("Add new habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "title", value = "Title", defaultValue = ""),
            @ApiImplicitParam(name = "description", value = "Description", defaultValue = ""),
            @ApiImplicitParam(name = "icon", value = "Icon", defaultValue = ""),
            @ApiImplicitParam(name = "schedule", value = "Schedule", defaultValue = "{\"from\":{\"hour\":5,\"minute\":0},\"to\":{\"hour\":5,\"minute\":30},\"repetition\":\"weekly\",\"times\":[\"mon\",\"tue\",\"wed\",\"thu\",\"fri\"],\"reminders\":[0]}"),
            @ApiImplicitParam(name = "tags", value = "Tags", defaultValue = "[]")
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "Habit has been created"),
            @ApiResponse(code = 409, message = "Habit has already existed")
    })
    ResponseEntity add(String username, String title, String description,
                       String icon, String schedule, String tags);

    @ApiOperation("Update a habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "id", defaultValue = "", value = "Habit Id"),
            @ApiImplicitParam(name = "title", value = "Title", defaultValue = ""),
            @ApiImplicitParam(name = "description", value = "Description", defaultValue = ""),
            @ApiImplicitParam(name = "icon", value = "Icon", defaultValue = ""),
            @ApiImplicitParam(name = "schedule", value = "Schedule", defaultValue = "{\"from\":{\"hour\":5,\"minute\":0},\"to\":{\"hour\":5,\"minute\":30},\"repetition\":\"weekly\",\"times\":[\"mon\",\"tue\",\"wed\",\"thu\",\"fri\"],\"reminders\":[0]}"),
            @ApiImplicitParam(name = "tags", value = "Tags", defaultValue = "[]")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Habit has been updated"),
            @ApiResponse(code = 404, message = "Habit not found")
    })
    ResponseEntity update(String username, String id, String title, String description,
                       String icon, String schedule, String tags);

    @ApiOperation("Stop a habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "habitId", defaultValue = "", value = "Habit Id")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Habit has been stopped"),
            @ApiResponse(code = 404, message = "Habit not found")
    })
    ResponseEntity stopHabit(String habitId, String username);

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

}
