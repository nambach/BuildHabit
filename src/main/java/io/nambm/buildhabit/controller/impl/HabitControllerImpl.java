package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HabitController;
import io.nambm.buildhabit.model.HabitModel;
import io.nambm.buildhabit.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class HabitControllerImpl implements HabitController {

    private final HabitService habitService;

    @Autowired
    public HabitControllerImpl(HabitService habitService) {
        this.habitService = habitService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/habit/add")
    public ResponseEntity add(@RequestParam String username,
                              @RequestParam String title,
                              @RequestParam String description) {
        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(new Date().toString());
        habitModel.setTitle(title);
        habitModel.setDescription(description);

        HttpStatus status = habitService.insert(habitModel);
        return new ResponseEntity(status);
    }

    @GetMapping("/habit/all")
    public ResponseEntity<List<HabitModel>> getAllHabits(@RequestParam String username,
                                                         @RequestParam String equalCondition) {
        return habitService.getAllHabits(username, equalCondition);
    }
}
