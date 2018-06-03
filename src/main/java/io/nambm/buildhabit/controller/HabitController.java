package io.nambm.buildhabit.controller;

import io.nambm.buildhabit.model.HabitModel;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(description = "Habit API")
public interface HabitController {

    @ApiOperation("Add new habit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "title", value = "Title", defaultValue = ""),
            @ApiImplicitParam(name = "description", value = "Description", defaultValue = "")
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "User has been created"),
            @ApiResponse(code = 409, message = "User has already existed")
    })
    ResponseEntity add(String username, String title, String description);

    @ApiOperation("Load all users")
    @ApiImplicitParam(name = "n", defaultValue = "10", value = "Quantity")
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<HabitModel>> getAllHabits(String username, String equalCondition);
}
