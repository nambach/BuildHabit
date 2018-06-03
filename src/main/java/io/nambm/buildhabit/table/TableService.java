package io.nambm.buildhabit.table;

import java.util.List;

public interface TableService<T> {

    boolean insertOrReplace(T entity);

    boolean insertOrMerge(T entity);

    boolean insertOrReplaceBatch(List<T> entities);

    boolean insertOrMergeBatch(List<T> entities);

    T delete(T entity);

    T search(String partitionKey, String rowKey);

    List<T> searchByPartition(String partitionKey);

    List<T> searchByPartition(String partitionKey, String equalConditions);

    List<T> searchTop(int count);

    List<T> searchTop(String partitionKey, int count);
}
