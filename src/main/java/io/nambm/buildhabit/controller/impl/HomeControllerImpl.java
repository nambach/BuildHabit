package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HomeController;
import io.nambm.buildhabit.model.UserModel;
import io.nambm.buildhabit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class HomeControllerImpl implements HomeController {

    private final UserService userService;

    @Autowired
    public HomeControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("home/home");
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
    public ResponseEntity<List<UserModel>> getAll(@RequestParam int n) {
        return userService.getAll(n);
    }
}
