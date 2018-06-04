package io.nambm.buildhabit.controller;

import io.nambm.buildhabit.model.HabitModel;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

@Api(description = "Habit API")
public interface HabitController {

    @ApiOperation("Add new habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "title", value = "Title", defaultValue = ""),
            @ApiImplicitParam(name = "description", value = "Description", defaultValue = ""),
            @ApiImplicitParam(name = "tags", value = "Tags", defaultValue = "[]"),
            @ApiImplicitParam(name = "startTime", value = "Start Time", defaultValue = ""),
            @ApiImplicitParam(name = "endTime", value = "End Time", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "User has been created"),
            @ApiResponse(code = 409, message = "User has already existed")
    })
    ResponseEntity add(String username, String title, String description,
                       List<String> tags, String startTime, String endTime);

    @ApiOperation("Load all habits")
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<HabitModel>> getAllHabits(String username, String equalCondition);
}
