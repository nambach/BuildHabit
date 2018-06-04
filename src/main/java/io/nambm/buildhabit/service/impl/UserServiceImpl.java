package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.UserBusiness;
import io.nambm.buildhabit.model.UserModel;
import io.nambm.buildhabit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserBusiness userBusiness;

    @Autowired
    public UserServiceImpl(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    @Override
    public HttpStatus insert(UserModel model) {
        return userBusiness.insert(model) ? HttpStatus.CREATED : HttpStatus.CONFLICT;
    }

    @Override
    public HttpStatus update(UserModel model) {
        return userBusiness.update(model) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }

    @Override
    public HttpStatus remove(UserModel model) {
        return userBusiness.remove(model) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }

    @Override
    public ResponseEntity<UserModel> get(String username) {
        UserModel model;
        if (username != null) {
            model = userBusiness.get(username);
        } else {
            model = null;
        }
        HttpStatus status = model != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(model, status);
    }

    @Override
    public ResponseEntity<List<UserModel>> getAll(String equalConditions) {
        return new ResponseEntity<>(userBusiness.getAll(equalConditions), HttpStatus.OK);
    }
}
