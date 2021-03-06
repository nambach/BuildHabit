package io.nambm.buildhabit.service;

import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.model.user.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    HttpStatus insert(UserModel model);

    HttpStatus updateNameInfo(UserModel model);

    HttpStatus updateStatus(UserModel model);

    HttpStatus remove(UserModel model);

    ResponseEntity<UserModel> get(String username);

    ResponseEntity<List<UserModel>> getAll(String equalConditions);

    ResponseEntity<BootgridResponse<UserModel>> getPage(int rowCount, int currentPage);
}
