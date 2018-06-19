package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HabitController;
import io.nambm.buildhabit.model.habit.DailyHabit;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.habit.Schedule;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.util.JsonUtils;
import io.nambm.buildhabit.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HabitControllerImpl implements HabitController {

    private final HabitService habitService;
    private final HabitLogService habitLogService;

    @Autowired
    public HabitControllerImpl(HabitService habitService, HabitLogService habitLogService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/habit/add")
    public ResponseEntity add(@RequestParam String username,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam String icon,
                              @RequestParam String schedule,
                              @RequestParam String tags) {
        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(username + "_" + System.currentTimeMillis());

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

    @PutMapping("/habit/check")
    public ResponseEntity checkDone(String username, String habitId, long time, int offsetMillis) {
        HttpStatus status = habitLogService.addLog(username, habitId, time, offsetMillis)
                ? HttpStatus.OK
                : HttpStatus.NOT_FOUND;

        return new ResponseEntity(status);
    }

    @PutMapping("/habit/un-check")
    public ResponseEntity undoCheckDone(String username, String habitId, long time, int offsetMillis) {
        HttpStatus status = habitLogService.deleteLog(username, habitId, time, offsetMillis)
                ? HttpStatus.OK
                : HttpStatus.NOT_FOUND;

        return new ResponseEntity(status);
    }

    @GetMapping("/habit/all")
    public ResponseEntity<List<HabitModel>> getAllHabits(@RequestParam String username) {
        return habitService.getAllHabits(username, "{}");
    }

//    @GetMapping("/habit/this-week")
    public ResponseEntity<List<DailyHabit>> getCurrentWeekHabits(@RequestParam String username,
                                                                 @RequestParam int offsetMillis) {
        return habitService.getThisWeekHabits(username, "{}", offsetMillis);
    }

    @GetMapping("/habit/by-time")
    public ResponseEntity<List<DailyHabit>> getHabits(@RequestParam String username,
                                                      @RequestParam String from,
                                                      @RequestParam String to,
                                                      @RequestParam int offsetMillis) {
        Logger logger = LoggerFactory.getLogger(HabitControllerImpl.class);
        logger.info("username" + ":" + username);

        long fromTime = TimeUtils.getTimeMillis(from, TimeUtils.MM_DD_YYYY);
        long toTime = TimeUtils.getTimeMillis(to, TimeUtils.MM_DD_YYYY);

        if (fromTime <= 0 || toTime <= 0) {
            logger.info("status" + ":" + HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            ResponseEntity<List<DailyHabit>> responseEntity = habitService.getHabitsByDateRange(fromTime, toTime, username, "{}", offsetMillis);
            logger.info("status:" + HttpStatus.OK.toString());
            return responseEntity;
        }
    }
}
