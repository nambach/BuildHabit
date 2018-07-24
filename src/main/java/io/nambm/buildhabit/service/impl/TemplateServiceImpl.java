package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.model.habit.HabitModel;
import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.nambm.buildhabit.constant.AppConstant.TEMPLATE;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private HabitBusiness habitBusiness;

    @Override
    public BootgridResponse<HabitModel> getPage(int current, int rowCount) {
        return habitBusiness.getPage(rowCount, current, TEMPLATE, null);
    }
}
