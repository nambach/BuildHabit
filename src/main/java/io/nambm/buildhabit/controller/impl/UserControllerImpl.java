package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.UserController;
import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.model.user.UserModel;
import io.nambm.buildhabit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user-management")
    public ModelAndView handleHome() {
        return new ModelAndView("user/user-management");
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
                                 @RequestParam String name,
                                 @RequestParam String info) {
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setName(name);
        userModel.setInfo(info);

        HttpStatus status = userService.updateNameInfo(userModel);
        return new ResponseEntity(status);
    }

    @PutMapping("/user/activate")
    public ResponseEntity activateUser(@RequestParam String username) {
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setAccountStatus(UserModel.ACC_ACTIVATED);

        HttpStatus status = userService.updateStatus(userModel);
        return new ResponseEntity(status);
    }

    @PutMapping("/user/block")
    public ResponseEntity blockUser(@RequestParam String username) {
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setAccountStatus(UserModel.ACC_BLOCKED);

        HttpStatus status = userService.updateStatus(userModel);
        return new ResponseEntity(status);
    }

    @PutMapping("/user/deactivate")
    public ResponseEntity deactivateUser(@RequestParam String username) {
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setAccountStatus(UserModel.ACC_DEACTIVATED);

        HttpStatus status = userService.updateStatus(userModel);
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

    @GetMapping("/user/page")
    public ResponseEntity<BootgridResponse<UserModel>> getPage(@RequestParam int rowCount,
                                                               @RequestParam int current) {
        return userService.getPage(rowCount, current);
    }
}
