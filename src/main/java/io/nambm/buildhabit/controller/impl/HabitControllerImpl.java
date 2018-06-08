package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HabitController;
import io.nambm.buildhabit.model.habit.DailyHabit;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.habit.Schedule;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.util.JsonUtils;
import io.nambm.buildhabit.util.TimeUtils;
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
                              @RequestParam String schedule,
                              @RequestParam String tags,
                              @RequestParam long startTime,
                              @RequestParam long endTime) {
        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(username + "_" + System.currentTimeMillis());

        habitModel.setTitle(title);
        habitModel.setDescription(description);

        habitModel.setSchedule(Schedule.from(schedule));
        habitModel.setTags(JsonUtils.getArray(tags, String.class));

        habitModel.setStartTime(startTime);
        habitModel.setEndTime(endTime);

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

    @GetMapping("/habit/all")
    public ResponseEntity<List<HabitModel>> getAllHabits(@RequestParam String username,
                                                         @RequestParam String equalCondition) {
        return habitService.getAllHabits(username, equalCondition);
    }

    @GetMapping("/habit/this-week")
    public ResponseEntity<List<DailyHabit>> getCurrentWeekHabits(@RequestParam String username,
                                                                 @RequestParam int offsetMillis) {
        return habitService.getThisWeekHabits(username, "{}", offsetMillis);
    }

    @GetMapping("/habit/by-time")
    public ResponseEntity<List<DailyHabit>> getHabits(@RequestParam String username,
                                                      @RequestParam String from,
                                                      @RequestParam String to,
                                                      @RequestParam int offsetMillis) {
        long fromTime = TimeUtils.getTimeMillis(from, TimeUtils.DD_MM_YYYY);
        long toTime = TimeUtils.getTimeMillis(to, TimeUtils.DD_MM_YYYY);

        if (fromTime <= 0 || toTime <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return habitService.getHabitsByDateRange(fromTime, toTime, username, "{}", offsetMillis);
        }
    }
}
