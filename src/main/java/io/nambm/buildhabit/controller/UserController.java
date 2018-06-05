package io.nambm.buildhabit.controller;

import io.nambm.buildhabit.model.user.UserModel;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(description = "User API")
public interface UserController {

    @ApiOperation("Add new user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", defaultValue = "nambm", value = "Username"),
            @ApiImplicitParam(name = "password", value = "Password", defaultValue = "123"),
            @ApiImplicitParam(name = "name", value = "Name", defaultValue = "Bach Minh Nam")
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "User has been created"),
            @ApiResponse(code = 409, message = "User has already existed")
    })
    ResponseEntity add(String username, String password, String name);

    @ApiOperation("Update user")
    ResponseEntity update(String username, String password, String name);

    @ApiOperation("Get a user information")
    ResponseEntity<UserModel> get(String username);

    @ApiOperation("Load all users")
    @ApiImplicitParam(name = "equalConditions", defaultValue = "{\"PartitionKey\" : null}", value = "Equal Search Conditions (json)")
    @ApiResponse(code = 200, message = "Found")
    ResponseEntity<List<UserModel>> getAll(String equalConditions);
}
