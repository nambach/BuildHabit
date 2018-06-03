package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.HabitBusiness;
import io.nambm.buildhabit.model.HabitModel;
import io.nambm.buildhabit.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitServiceImpl implements HabitService {

    private final HabitBusiness habitBusiness;

    @Autowired
    public HabitServiceImpl(HabitBusiness habitBusiness) {
        this.habitBusiness = habitBusiness;
    }

    @Override
    public HttpStatus insert(HabitModel model) {
        return habitBusiness.insert(model) ? HttpStatus.CREATED : HttpStatus.CONFLICT;
    }

    @Override
    public HttpStatus update(HabitModel model) {
        return null;
    }

    @Override
    public HttpStatus remove(HabitModel model) {
        return null;
    }

    @Override
    public ResponseEntity<List<HabitModel>> getAllHabits(String username, String equalConditions) {
        return new ResponseEntity<>(habitBusiness.getAllHabits(username, equalConditions), HttpStatus.OK);
    }
}
