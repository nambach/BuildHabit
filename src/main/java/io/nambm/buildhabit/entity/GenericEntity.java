package io.nambm.buildhabit.entity;

import com.microsoft.azure.storage.table.TableServiceEntity;

public abstract class GenericEntity<T> extends TableServiceEntity {
    public abstract T toModel();
}
