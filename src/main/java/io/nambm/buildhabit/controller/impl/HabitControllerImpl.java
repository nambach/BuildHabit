package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HabitController;
import io.nambm.buildhabit.model.habit.DailyHabit;
import io.nambm.buildhabit.model.habit.DailyHabitModel;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.habitlog.StatisticResponse;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.service.TagService;
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
import java.util.stream.Collectors;

@RestController
public class HabitControllerImpl implements HabitController {

    private Logger logger = LoggerFactory.getLogger(HabitControllerImpl.class);
    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final TagService tagService;

    @Autowired
    public HabitControllerImpl(HabitService habitService, HabitLogService habitLogService, TagService tagService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.tagService = tagService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/habit/add")
    public ResponseEntity<String> addV2(@RequestBody String body) {
        logger.info("Start: /habit/add");

        HabitModel habitModel = HabitModel.parseRequest(body);

        habitModel.setId(habitModel.generateId());

        HttpStatus status = habitService.insert(habitModel);

        logger.info("End: /habit/add, " + status);
        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @PutMapping("/habit/update")
    public ResponseEntity<String> updateV2(@RequestBody String body) {
        logger.info("Start: /habit/update");

        HabitModel habitModel = HabitModel.parseRequest(body);

        HttpStatus status = habitService.update(habitModel);

        logger.info("End: /habit/update, " + status);
        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @PutMapping("/habit/stop")
    public ResponseEntity<String> stopHabitV2(@RequestBody String body) {
        logger.info("Start: /habit/stop");

        String username = JsonUtils.getValue(body, "username");
        String habitId = JsonUtils.getValue(body, "habitId");

        HabitModel habitModel = new HabitModel();
        habitModel.setUsername(username);
        habitModel.setId(habitId);

        HttpStatus status = habitService.stopHabit(habitModel);

        logger.info("End: /habit/stop, " + status);
        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @PutMapping("/habit/check")
    public ResponseEntity<String> checkDoneV2(@RequestBody String body) {
        String username = JsonUtils.getValue(body, "username");
        String habitId = JsonUtils.getValue(body, "habitId");
        long time = 0;
        int offsetMillis = 0;
        try {
            time = JsonUtils.getValue(body, "time", Long.class);
            offsetMillis = JsonUtils.getValue(body, "offsetMillis", Integer.class);
        } catch (Exception e) {
            return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, HttpStatus.BAD_REQUEST);
        }

        HttpStatus status = habitLogService.addLog(username, habitId, time, offsetMillis);

        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @PutMapping("/habit/un-check")
    public ResponseEntity<String> undoCheckDone(@RequestBody String body) {
        String username = JsonUtils.getValue(body, "username");
        String habitId = JsonUtils.getValue(body, "habitId");
        long time = 0;
        int offsetMillis = 0;
        try {
            time = JsonUtils.getValue(body, "time", Long.class);
            offsetMillis = JsonUtils.getValue(body, "offsetMillis", Integer.class);
        } catch (Exception e) {
            return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, HttpStatus.BAD_REQUEST);
        }

        HttpStatus status = habitLogService.deleteLog(username, habitId, time, offsetMillis);

        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @PutMapping("/habit/edit-tags")
    public ResponseEntity<String> editTags(String body) {
        logger.info("Start: /habit/edit-tags");
        HttpStatus status = HttpStatus.OK;

        String id = JsonUtils.getValue(body, "id");
        String action = JsonUtils.getValue(body, "action");
        List<String> tagList = JsonUtils.getArray(JsonUtils.getValue(body, "tags"), String.class);

        if ("add".equalsIgnoreCase(action)) {
            status = tagService.addTagsToHabit(null, id, tagList);
        } else if ("remove".equalsIgnoreCase(action)) {
            status = tagService.removeTagsFromHabit(null, id, tagList);
        }

        logger.info("End: /habit/edit-tags, " + status);
        return new ResponseEntity<>(JsonUtils.EMPTY_OBJECT, status);
    }

    @GetMapping("/habit/get")
    public ResponseEntity<HabitModel> get(String username, String habitId) {
        logger.info("Start: /habit/get");

        HabitModel stubModel = new HabitModel();
        stubModel.setUsername(username);
        stubModel.setId(habitId);

        ResponseEntity<HabitModel> responseEntity = habitService.get(stubModel);

        logger.info("End: /habit/get, " + responseEntity.getStatusCode());
        return responseEntity;
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
        logger.info("Start: /habit/by-time, username=" + username);

        long fromTime = TimeUtils.getTimeMillis(from, TimeUtils.MM_DD_YYYY, offsetMillis);
        long toTime = TimeUtils.getTimeMillis(to, TimeUtils.MM_DD_YYYY, offsetMillis);

        if (fromTime <= 0 || toTime <= 0) {
            logger.info("End: /habit/by-time, status=" + HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            ResponseEntity<List<DailyHabit>> responseEntity = habitService.getHabitsByDateRange(fromTime, toTime, username, "{}", offsetMillis);
            logger.info("End: /habit/by-time status=" + HttpStatus.OK.toString());
            return responseEntity;
        }
    }

    @GetMapping("/habit/by-time-offset")
    public ResponseEntity<List<DailyHabitModel>> getHabitsByDateOffset(@RequestParam String username,
                                                                       @RequestParam String mode,
                                                                       @RequestParam int dateOffset,
                                                                       @RequestParam int offsetMillis) {
        logger.info("Start: /habit/by-time-offset, username=" + username);

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
        List<DailyHabitModel> result = new LinkedList<>();

        // Fetch habits grouped by dates
        ResponseEntity<List<DailyHabit>> responseEntity = habitService.getHabitsByDateRange(from, to, username, "{}", offsetMillis);

        // Collect the habitModel from each date and put into result
        responseEntity.getBody().forEach(dailyHabit -> dailyHabitModels.addAll(dailyHabit.getHabits()));

        // Sort by chronological order
        if (TimeUtils.FUTURE.equals(mode)) {
            result = dailyHabitModels
                    .stream()
                    .filter(dailyHabitModel -> dailyHabitModel.getTime() > current)
                    .collect(Collectors.toList());
            result.sort((o1, o2) -> Math.toIntExact(o1.getTime() - o2.getTime()));
        } else if (TimeUtils.PAST.equals(mode)) {
            result = dailyHabitModels
                    .stream()
                    .filter(dailyHabitModel -> dailyHabitModel.getTime() < current)
                    .collect(Collectors.toList());
            result.sort((o1, o2) -> Math.toIntExact(o2.getTime() - o1.getTime()));
        }

        logger.info("End: /habit/by-time-offset, status=" + responseEntity.getStatusCode());
        return new ResponseEntity<>(result, responseEntity.getStatusCode());
    }

    @GetMapping("/habit/get-logs")
    public ResponseEntity<StatisticResponse> getLogs(String username, String habitId, int offsetMillis) {
        logger.info("Start: /habit/get-logs, habitId=" + habitId);

        StatisticResponse response = habitLogService.getLogs(username, habitId, offsetMillis);

        logger.info("End: /habit/get-logs, status=" + HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/habit/get-by-tag")
    public ResponseEntity<List<HabitModel>> getHabitsByTag(String username, String tagName) {
        logger.info("Start: /habit/get-by-tag, username=" + username + ", tagName=" + tagName);

        if ("null".equalsIgnoreCase(username)) username = null;
        List<HabitModel> models = habitService.getByTags(username, tagName);

        logger.info("End: /habit/get-by-tag, status=" + HttpStatus.OK);
        return new ResponseEntity<>(models, HttpStatus.OK);
    }
}
