package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.UserController;
import io.nambm.buildhabit.model.UserModel;
import io.nambm.buildhabit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/add")
    public ResponseEntity add(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String name) {
        UserModel userModel = new UserModel(username, password, name, "");
        HttpStatus status = userService.insert(userModel);
        return new ResponseEntity(status);
    }

    @PutMapping("/user/update")
    public ResponseEntity update(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String name) {
        UserModel userModel = new UserModel(username, password, name, "");
        HttpStatus status = userService.update(userModel);
        return new ResponseEntity(status);
    }

    @GetMapping("/user")
    public ResponseEntity<UserModel> get(String username) {
        return userService.get(username);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserModel>> getAll(@RequestParam String equalConditions) {
        return userService.getAll(equalConditions);
    }
}