package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.GenericBusiness;
import io.nambm.buildhabit.entity.GenericEntity;
import io.nambm.buildhabit.model.GenericModel;
import io.nambm.buildhabit.model.submodel.BootgridResponse;
import io.nambm.buildhabit.table.impl.TableServiceImpl;
import io.nambm.buildhabit.util.GenericClassUtils;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class GenericBusinessImpl<M extends GenericModel<E>, E extends GenericEntity<M>> implements GenericBusiness<M> {

    private TableServiceImpl<E> tableService;

    private Class<M> modelClass;

    public GenericBusinessImpl() {
        this.tableService = new TableServiceImpl<>();
        this.modelClass = GenericClassUtils.getGenericClass(this.getClass());
    }

    @Override
    public HttpStatus insert(M model) {
        if (tableService.getEntity(model.getPartitionKey(), model.getRowKey()) == null) {
            return tableService.insertOrReplace(model.toEntity())
                    ? HttpStatus.CREATED
                    : HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.CONFLICT;
        }
    }

    @Override
    public HttpStatus update(M model, String... properties) {
        E currentEntity = tableService.getEntity(model.getPartitionKey(), model.getRowKey());
        if (currentEntity != null) {
            M currentModel = currentEntity.toModel();

            for (String property : properties) {
                try {
                    Field field = modelClass.getDeclaredField(property);
                    field.setAccessible(true);
                    field.set(currentModel, field.get(model));
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }

            return tableService.insertOrReplace(currentModel.toEntity())
                    ? HttpStatus.OK
                    : HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    @Override
    public HttpStatus remove(M model) {
        E entity = tableService.getEntity(model.getPartitionKey(), model.getRowKey());
        if (entity != null) {
            return tableService.delete(entity) != null
                    ? HttpStatus.OK
                    : HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    @Override
    public M get(M model) {
        E entity = tableService.getEntity(model.getPartitionKey(), model.getRowKey());
        if (entity != null) {
            return entity.toModel();
        } else {
            return null;
        }
    }

    @Override
    public List<M> getAll(String partitionKey, String equalConditions, String tableServiceQueryFilter) {
        List<E> entities = tableService.searchAll(partitionKey, equalConditions, tableServiceQueryFilter);
        if (entities != null) {
            return entities.stream().map(E::toModel).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public BootgridResponse<M> getPage(int rowCount, int currentPage, String partitionKey, String tableServiceQueryFilter) {
        BootgridResponse<E> entities = tableService.searchPage(rowCount, currentPage, partitionKey, tableServiceQueryFilter);
        BootgridResponse<M> models = null;
        try {
            models = new BootgridResponse<>(
                    entities.getCurrent(),
                    entities.getRowCount(),
                    entities.getTotal(),
                    entities.getRows().stream().map(E::toModel).collect(Collectors.toList())
            );
        } catch (Exception ignored) {
        }
        return models;
    }
}
