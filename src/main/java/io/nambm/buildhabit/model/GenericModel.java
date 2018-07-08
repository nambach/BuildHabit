package io.nambm.buildhabit.model;

import com.microsoft.azure.storage.table.TableServiceEntity;

public interface GenericModel<T extends TableServiceEntity> {
    String getPartitionKey();
    String getRowKey();
    T toEntity();
}
