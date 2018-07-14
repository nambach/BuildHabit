package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.business.TagBusiness;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.tag.TagModel;
import io.nambm.buildhabit.service.TagService;
import io.nambm.buildhabit.table.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.nambm.buildhabit.table.util.QueryUtils.getEqualFilter;

@Service
public class TagServiceImpl implements TagService {

    private final TagBusiness tagBusiness;
    private final HabitBusiness habitBusiness;

    @Autowired
    public TagServiceImpl(TagBusiness tagBusiness, HabitBusiness habitBusiness) {
        this.tagBusiness = tagBusiness;
        this.habitBusiness = habitBusiness;
    }

    @Override
    public HttpStatus importTagsFrom(HabitModel habitModel) {
        // Return if having no tag
        if (habitModel.getTags().isEmpty()) {
            return HttpStatus.OK;
        }

        List<String> newTags = habitModel.getTags();
        TagModel tagModel = new TagModel();
        tagModel.setHabitId(habitModel.getId());
        tagModel.setPrivateMode(habitModel.getPrivateMode());
        tagModel.setUsername(habitModel.getUsername());
        tagModel.setStatus(TagModel.Status.PENDING);

        // Get current tags
        List<String> currentTags = tagBusiness
                .getAll(null, null, getEqualFilter("HabitId", tagModel.getHabitId()))
                .stream()
                .map(TagModel::getTagName)
                .collect(Collectors.toList());

        // Insert new tags
        for (String newTag : newTags) {
            if (currentTags.contains(newTag)) {
                continue;
            }

            tagModel.setTagName(newTag);
            tagBusiness.insert(tagModel);
        }

        // Remove old tags that disappear from the newest tags
        for (String currentTag : currentTags) {
            if (newTags.contains(currentTag)) {
                continue;
            }

            tagModel.setTagName(currentTag);
            tagBusiness.remove(tagModel);
        }
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus addTagsToHabit(String username, String habitId, List<String> tagNames) {
        // Return if having no tag
        if (tagNames.isEmpty()) {
            return HttpStatus.OK;
        }

        HabitModel habitModel = username == null
                ? habitBusiness.get(habitId)
                : habitBusiness.get(username, habitId);

        if (habitModel == null) {
            return HttpStatus.NOT_FOUND;
        }

        // Add tags into habit
        addAllDistinct(habitModel.getTags(), tagNames);
        habitBusiness.update(habitModel, "tags");

        // Prepare tag sample to insert
        TagModel tagModel = new TagModel();
        tagModel.setStatus(TagModel.Status.PENDING);
        tagModel.setUsername(habitModel.getUsername());
        tagModel.setPrivateMode(habitModel.getPrivateMode());
        tagModel.setHabitId(habitModel.getId());

        // Get current tag entries
        List<TagModel> currentTags = tagBusiness
                .getAll(null, null, getEqualFilter("HabitId", habitId));

        // Insert new tags
        for (String tagName : tagNames) {
            if (currentTags.stream().anyMatch(tag -> tag.getTagName().equals(tagName))) {
                continue;
            }

            tagModel.setTagName(tagName);
            tagBusiness.insert(tagModel);
        }

        return HttpStatus.OK;
    }

    private <T> void addAllDistinct(List<T> container, List<T> elements) {
        Set<T> set = new HashSet<>(container);
        set.addAll(elements);

        container.clear();
        container.addAll(set);
    }

    @Override
    public HttpStatus removeTagsFromHabit(String username, String habitId, List<String> tagNames) {
        // Return if having no tag
        if (tagNames.isEmpty()) {
            return HttpStatus.OK;
        }

        HabitModel habitModel = username == null
                ? habitBusiness.get(habitId)
                : habitBusiness.get(username, habitId);

        if (habitModel == null) {
            return HttpStatus.NOT_FOUND;
        }

        // Remove tags into habit
        habitModel.getTags().removeAll(tagNames);
        habitBusiness.update(habitModel, "tags");

        // Prepare tag sample to remove
        TagModel tagModel = new TagModel();
        tagModel.setStatus(TagModel.Status.PENDING);
        tagModel.setUsername(habitModel.getUsername());
        tagModel.setPrivateMode(habitModel.getPrivateMode());
        tagModel.setHabitId(habitModel.getId());

        // Get current tag entries
        List<TagModel> currentTags = tagBusiness
                .getAll(null, null, getEqualFilter("HabitId", habitId));

        // Remove target tags
        for (String tagName : tagNames) {
            if (currentTags.stream().anyMatch(tag -> tag.getTagName().equals(tagName))) {
                tagModel.setTagName(tagName);
                tagBusiness.remove(tagModel);
            }
        }

        return HttpStatus.OK;
    }

    @Override
    public List<String> getAllTags(String username) {
        List<TagModel> tags = tagBusiness.getAll(null, null, QueryUtils.getEqualFilter("Username", username));
        return tags.stream()
                .map(TagModel::getTagName)
                .distinct()
                .collect(Collectors.toList());
    }
}
