package io.nambm.buildhabit.controller.impl;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.nambm.buildhabit.controller.UserController;
import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.model.user.UserModel;
import io.nambm.buildhabit.service.UserService;
import io.nambm.buildhabit.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

import static io.nambm.buildhabit.constant.SecurityConstants.*;

@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserControllerImpl(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/user-management")
    public ModelAndView handleHome() {
        return new ModelAndView("user/user-management");
    }


    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<String> signUp(@RequestBody String body) {
        String username = JsonUtils.getValue(body, "username");
        String password = JsonUtils.getValue(body, "password");
        String name = JsonUtils.getValue(body, "name");
        String email = JsonUtils.getValue(body, "email");
        String info = JsonUtils.getValue(body, "info");

        if (password == null) {
            password = UserModel.DEFAULT_PASSWORD;
        }

        UserModel userModel = new UserModel(username, bCryptPasswordEncoder.encode(password), name, info);
        userModel.setRole(UserModel.Role.USER);
        userModel.setEmail(email);
        userModel.setAccountStatus(UserModel.ACC_ACTIVATED);

        HttpStatus status = userService.insert(userModel);
        if (status == HttpStatus.CONFLICT) {
            return new ResponseEntity<>("Username has already existed", HttpStatus.CONFLICT);
        } else if (status == HttpStatus.CREATED) {
            String token = Jwts.builder().setSubject(userModel.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                    .compact();
            return new ResponseEntity<>(new Gson().toJson(TOKEN_PREFIX + token), status);
        }

        return new ResponseEntity<>("No thing happened", status);
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
