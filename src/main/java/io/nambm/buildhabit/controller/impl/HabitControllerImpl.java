package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HabitController;
import io.nambm.buildhabit.model.HabitModel;
import io.nambm.buildhabit.model.submodel.Schedule;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.util.JsonUtils;
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
                              @RequestParam String description,
                              @RequestParam String schedule,
                              @RequestParam String tags,
                              @RequestParam String startTime,
                              @RequestParam String endTime) {
        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(new Date().getTime() + "");

        habitModel.setTitle(title);
        habitModel.setDescription(description);

        habitModel.setSchedule(Schedule.from(schedule));
        habitModel.setTags(JsonUtils.getArray(tags, String.class));

        habitModel.setStartTime(Long.parseLong(startTime));
        habitModel.setEndTime(Long.parseLong(endTime));

        HttpStatus status = habitService.insert(habitModel);
        return new ResponseEntity(status);
    }

    @GetMapping("/habit/all")
    public ResponseEntity<List<HabitModel>> getAllHabits(@RequestParam String username,
                                                         @RequestParam String equalCondition) {
        return habitService.getAllHabits(username, equalCondition);
    }
}
