package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.TemplateController;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.tag.TagHabitsResponse;
import io.nambm.buildhabit.model.tag.TagModel;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.service.TagService;
import io.nambm.buildhabit.table.BlobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TemplateControllerImpl implements TemplateController {

    private static final String TEMPLATE = "template";

    Logger logger = LoggerFactory.getLogger(TemplateControllerImpl.class);

    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final TagService tagService;
    private final BlobService blobService;

    @Autowired
    public TemplateControllerImpl(HabitService habitService, HabitLogService habitLogService, TagService tagService, BlobService blobService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.tagService = tagService;
        this.blobService = blobService;
    }

    @GetMapping("/habit-template/all")
    public ResponseEntity<List<HabitModel>> getTemplateHabits() {
        ResponseEntity<List<HabitModel>> responseEntity = habitService.getAllHabits(TEMPLATE, "{}");

        logger.info("getTemplateHabits: status=" + responseEntity.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/upload-img")
    public ResponseEntity<String> uploadImage(String image) {
        try {
            image = image.substring(image.indexOf(",") + 1);

            byte[] decodedString = Base64.getDecoder().decode(image.getBytes("UTF-8"));
            String uri = blobService.upload("images", "nambm", decodedString);
            return new ResponseEntity<>(uri, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<TagHabitsResponse>> getSuggestions() {
        List<TagHabitsResponse> responses = new LinkedList<>();

        List<String> tagNames = tagService.getAllTags(TEMPLATE);
        List<HabitModel> habitModels = habitService.getAllHabits(TEMPLATE, null).getBody();

        for (String tagName : tagNames) {
            TagHabitsResponse response = new TagHabitsResponse();
            response.setTagName(tagName);

            List<HabitModel> habits = habitModels
                    .stream()
                    .filter(habitModel -> habitModel.getTags().contains(tagName))
                    .collect(Collectors.toList());
            response.setHabits(habits);

            responses.add(response);
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
