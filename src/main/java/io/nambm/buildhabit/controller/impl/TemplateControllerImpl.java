package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.TemplateController;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.model.tag.TagHabitsResponse;
import io.nambm.buildhabit.service.HabitLogService;
import io.nambm.buildhabit.service.HabitService;
import io.nambm.buildhabit.service.TagService;
import io.nambm.buildhabit.service.TemplateService;
import io.nambm.buildhabit.table.BlobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static io.nambm.buildhabit.constant.AppConstant.TEMPLATE;

@RestController
public class TemplateControllerImpl implements TemplateController {

    Logger logger = LoggerFactory.getLogger(TemplateControllerImpl.class);

    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final TemplateService templateService;
    private final TagService tagService;
    private final BlobService blobService;

    @Autowired
    public TemplateControllerImpl(HabitService habitService, HabitLogService habitLogService, TemplateService templateService, TagService tagService, BlobService blobService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.templateService = templateService;
        this.tagService = tagService;
        this.blobService = blobService;
    }

    @GetMapping("/template-management")
    public ModelAndView handle() {
        return new ModelAndView("habit_template/template-management");
    }

    @GetMapping("/template/page")
    public ResponseEntity<BootgridResponse<HabitModel>> getPage(int current, int rowCount) {
        BootgridResponse<HabitModel> bootgridResponse = templateService.getPage(current, rowCount);
        return new ResponseEntity<>(bootgridResponse, HttpStatus.OK);
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
                    .filter(habitModel -> HabitModel.PRIVATE_MODE.PUBLIC.equalsIgnoreCase(habitModel.getPrivateMode()))
                    .collect(Collectors.toList());
            response.setHabits(habits);

            responses.add(response);
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
