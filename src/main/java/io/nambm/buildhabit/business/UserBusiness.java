package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.model.user.UserModel;

import java.util.List;

public interface UserBusiness {
    boolean insert(UserModel model);
    boolean update(UserModel model);
    boolean remove(UserModel model);
    UserModel get(String username);
    List<UserModel> getAll(String equalConditions);
    BootgridResponse<UserModel> getPage(int rowCount, int currentPage);

}
