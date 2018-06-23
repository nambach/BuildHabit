package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HabitController;
import io.nambm.buildhabit.model.habit.DailyHabit;
import io.nambm.buildhabit.model.habit.DailyHabitModel;
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

import java.util.LinkedList;
import java.util.List;

@RestController
public class HabitControllerImpl implements HabitController {

    private Logger logger = LoggerFactory.getLogger(HabitControllerImpl.class);
    private final HabitService habitService;
    private final HabitLogService habitLogService;

    @Autowired
    public HabitControllerImpl(HabitService habitService, HabitLogService habitLogService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/habit/add-v1")
    public ResponseEntity add(@RequestParam String username,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam String icon,
                              @RequestParam String schedule,
                              @RequestParam String tags) {
        logger.info("/habit/add-v1");
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/habit/add")
    public ResponseEntity addV2(@RequestBody String body) {
        logger.info("/habit/add");
        HabitModel habitModel = new HabitModel();

        String username = JsonUtils.getValue(body, "username");
        String title = JsonUtils.getValue(body, "title");
        String description = JsonUtils.getValue(body, "description");
        String icon = JsonUtils.getValue(body, "icon");
        String schedule = JsonUtils.getValue(body, "schedule");
        String tags = JsonUtils.getValue(body, "tags");

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

    @GetMapping("/habit/get")
    public ResponseEntity<HabitModel> get(String username, String habitId) {
        HabitModel stubModel = new HabitModel();
        stubModel.setUsername(username);
        stubModel.setId(habitId);

        ResponseEntity<HabitModel> responseEntity = habitService.get(stubModel);
        logger.info("/habit/get status:" + HttpStatus.OK.toString());
        return responseEntity;
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

    @GetMapping("/habit/by-time")
    public ResponseEntity<List<DailyHabit>> getHabits(@RequestParam String username,
                                                      @RequestParam String from,
                                                      @RequestParam String to,
                                                      @RequestParam int offsetMillis) {
        logger.info("username" + ":" + username);

        long fromTime = TimeUtils.getTimeMillis(from, TimeUtils.MM_DD_YYYY);
        long toTime = TimeUtils.getTimeMillis(to, TimeUtils.MM_DD_YYYY);

        if (fromTime <= 0 || toTime <= 0) {
            logger.info("/habit/by-time status" + ":" + HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            ResponseEntity<List<DailyHabit>> responseEntity = habitService.getHabitsByDateRange(fromTime, toTime, username, "{}", offsetMillis);
            logger.info("/habit/by-time status:" + HttpStatus.OK.toString());
            return responseEntity;
        }
    }

    @GetMapping("/habit/by-time-offset")
    public ResponseEntity<List<DailyHabitModel>> getHabitsByDateOffset(@RequestParam String username,
                                                                       @RequestParam String mode,
                                                                       @RequestParam int dateOffset,
                                                                       @RequestParam int offsetMillis) {
        long current = System.currentTimeMillis();
        long timeOffset = TimeUtils.getByDateOffset(current, mode, dateOffset, TimeUtils.getCalendar(offsetMillis));

        long from, to;
        if (TimeUtils.FUTURE.equals(mode)) {
            from = current;
            to = timeOffset;
        } else if (TimeUtils.PAST.equals(mode)) {
            from = timeOffset;
            to = current;
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<DailyHabitModel> dailyHabitModels = new LinkedList<>();

        // Fetch habits grouped by dates
        ResponseEntity<List<DailyHabit>> responseEntity = habitService.getHabitsByDateRange(from, to, username, "{}", offsetMillis);

        // Collect the habitModel from each date and put into result
        responseEntity.getBody().forEach(dailyHabit -> dailyHabitModels.addAll(dailyHabit.getHabits()));

        // Sort by chronological order
        if (TimeUtils.FUTURE.equals(mode)) {
            dailyHabitModels.sort((o1, o2) -> Math.toIntExact(o1.getTime() - o2.getTime()));
        } else if (TimeUtils.PAST.equals(mode)) {
            dailyHabitModels.sort((o1, o2) -> Math.toIntExact(o2.getTime() - o1.getTime()));
        }

        return new ResponseEntity<>(dailyHabitModels, responseEntity.getStatusCode());
    }
}
