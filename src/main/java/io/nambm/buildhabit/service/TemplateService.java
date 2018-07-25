package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.submodel.BootgridResponse;

public interface TemplateService {

    BootgridResponse<HabitModel> getPage(int current, int rowCount);
}
