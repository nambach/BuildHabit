package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.TemplateController;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TemplateControllerImpl implements TemplateController {

    private static final String TEMPLATE = "template";

    Logger logger = LoggerFactory.getLogger(TemplateControllerImpl.class);

    private final HabitService habitService;
    private final HabitLogService habitLogService;

    @Autowired
    public TemplateControllerImpl(HabitService habitService, HabitLogService habitLogService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
    }

    @GetMapping("/habit-template/all")
    public ResponseEntity<List<HabitModel>> getTemplateHabits() {
        ResponseEntity<List<HabitModel>> responseEntity = habitService.getAllHabits(TEMPLATE, "{}");

        logger.info("getTemplateHabits: status=" + responseEntity.getStatusCode());
        return responseEntity;
    }


}
