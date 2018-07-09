package io.nambm.buildhabit.service.impl;

import com.microsoft.azure.storage.table.TableQuery;
import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.business.TagBusiness;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.tag.TagModel;
import io.nambm.buildhabit.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .getAll(null, null, getQueryFilter("HabitId", tagModel.getHabitId()))
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

    private static String getQueryFilter(String columnName, String value) {
        return TableQuery.generateFilterCondition(
                columnName,
                TableQuery.QueryComparisons.EQUAL,
                value
        );
    }

    @Override
    public HttpStatus addTagsToHabit(String username, String habitId, List<String> tagNames) {
        return null;
    }

    @Override
    public HttpStatus removeTagsFromHabit(String username, String habitId, List<String> tagNames) {
        return null;
    }
}
