package io.nambm.buildhabit.business;

import io.nambm.buildhabit.model.submodel.BootgridResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface GenericBusiness<T> {
    HttpStatus insert(T model);

    HttpStatus update(T model, String... properties);

    HttpStatus remove(T model);

    T get(T model);

    List<T> getAll(String partitionKey, String equalConditions, String tableServiceQueryFilter);

    BootgridResponse<T> getPage(int rowCount, int currentPage, String partitionKey, String tableServiceQueryFilter);
}
