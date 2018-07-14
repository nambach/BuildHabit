package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.UserBusiness;
import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.model.user.UserModel;
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
        return userBusiness.insert(model);
    }

    @Override
    public HttpStatus updateNameInfo(UserModel model) {
        return userBusiness.update(model, "name", "info");
    }

    @Override
    public HttpStatus updateStatus(UserModel model) {
        return userBusiness.update(model, "accountStatus");
    }

    @Override
    public HttpStatus remove(UserModel model) {
        return userBusiness.remove(model);
    }

    @Override
    public ResponseEntity<UserModel> get(String username) {
        UserModel model, wrapper = new UserModel();
        wrapper.setUsername(username);
        model = userBusiness.get(wrapper);
        HttpStatus status = model != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(model, status);
    }

    @Override
    public ResponseEntity<List<UserModel>> getAll(String equalConditions) {
        return new ResponseEntity<>(userBusiness.getAll(null, equalConditions, null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BootgridResponse<UserModel>> getPage(int rowCount, int currentPage) {
        return new ResponseEntity<>(userBusiness.getPage(rowCount, currentPage, null, null), HttpStatus.OK);
    }
}
