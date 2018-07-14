package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HabitControllerV1;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.habit.Schedule;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.service.TagService;
import io.nambm.buildhabit.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habit/v1")
public class HabitControllerV1Impl implements HabitControllerV1 {

    private Logger logger = LoggerFactory.getLogger(HabitControllerImpl.class);
    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final TagService tagService;

    @Autowired
    public HabitControllerV1Impl(HabitService habitService, HabitLogService habitLogService, TagService tagService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.tagService = tagService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public ResponseEntity add(@RequestParam String username,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam String icon,
                              @RequestParam String schedule,
                              @RequestParam String tags) {
        logger.info("/habit/v1/add");
        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(habitModel.generateId());

        habitModel.setTitle(title);
        habitModel.setDescription(description);
        habitModel.setIcon(icon);

        habitModel.setSchedule(Schedule.from(schedule));
        habitModel.setTags(JsonUtils.getArray(tags, String.class));

        habitModel.setStartTime(System.currentTimeMillis());
        habitModel.setEndTime(-1L);

        HttpStatus status = habitService.insert(habitModel);
        return new ResponseEntity(status);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestParam String username,
                                 @RequestParam String id,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String icon,
                                 @RequestParam String schedule,
                                 @RequestParam String tags) {
        logger.info("/habit/v1/update");
        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(id);

        habitModel.setTitle(title);
        habitModel.setDescription(description);
        habitModel.setIcon(icon);

        habitModel.setSchedule(Schedule.from(schedule));
        habitModel.setTags(JsonUtils.getArray(tags, String.class));

        HttpStatus status = habitService.update(habitModel);
        return new ResponseEntity(status);
    }

    @PutMapping("/edit-tags")
    public ResponseEntity editTags(String id, String tags, String action) {
        logger.info("/habit/v1/edit-tags");
        HttpStatus status = HttpStatus.OK;
        List<String> tagList = JsonUtils.getArray(tags, String.class);

        if ("add".equalsIgnoreCase(action)) {
            status = tagService.addTagsToHabit(null, id, tagList);
        } else if ("remove".equalsIgnoreCase(action)) {
            status = tagService.removeTagsFromHabit(null, id, tagList);
        }

        return new ResponseEntity(status);
    }

    @PutMapping("/stop")
    public ResponseEntity stopHabit(@RequestParam String habitId,
                                    @RequestParam String username) {
        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(habitId);

        HttpStatus status = habitService.stopHabit(habitModel);
        return new ResponseEntity(status);
    }

    @PutMapping("/check")
    public ResponseEntity checkDone(String username, String habitId, long time, int offsetMillis) {
        HttpStatus status = habitLogService.addLog(username, habitId, time, offsetMillis);

        return new ResponseEntity(status);
    }

    @PutMapping("/un-check")
    public ResponseEntity undoCheckDone(String username, String habitId, long time, int offsetMillis) {
        HttpStatus status = habitLogService.deleteLog(username, habitId, time, offsetMillis);

        return new ResponseEntity(status);
    }
}
