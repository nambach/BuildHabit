package io.nambm.buildhabit.table.impl;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.ResultContinuation;
import com.microsoft.azure.storage.ResultSegment;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.*;
import io.nambm.buildhabit.table.TableService;
import io.nambm.buildhabit.table.annotation.AzureTableName;
import io.nambm.buildhabit.util.JsonUtils;
import io.nambm.buildhabit.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableServiceImpl<T extends TableServiceEntity> implements TableService<T> {

    private static final String AZURE_ACC_NAME = "AZURE_STORAGE_ACCOUNT_NAME";
    private static final String AZURE_ACC_KEY = "AZURE_STORAGE_ACCOUNT_KEY";
    public static final String PARTITION_KEY = "PartitionKey";
    public static final String ROW_KEY = "RowKey";
    private static final int MAX_QUERY_COUNT = 1000;

    private CloudTable cloudTable;
    private String tableName;
    private Class<T> entityClass;

    public TableServiceImpl() {
        setEntityClass();
        setTableName();
        setCloudTable();
    }

    private void setEntityClass() {
        this.entityClass = (Class)((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    private void setTableName() {
        // Get table name
        AzureTableName tableName = entityClass.getAnnotation(AzureTableName.class);
        this.tableName = tableName.value();
    }

    private void setCloudTable() {
        String name = System.getenv(AZURE_ACC_NAME);
        String key = System.getenv(AZURE_ACC_KEY);

        String storageConnectionString =
                "DefaultEndpointsProtocol=https;" +
                        "AccountName=" + name +";" +
                        "AccountKey=" + key + ";" +
                        "TableEndpoint=https://" + name + ".table.core.windows.net;";

        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Create a cloud table object for the table.
            cloudTable = tableClient.getTableReference(tableName);

        } catch (URISyntaxException | InvalidKeyException | StorageException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insertOrReplace(T entity) {
        try {
            // Create an operation to add the new customer to the people table.
            TableOperation insertCustomer = TableOperation.insertOrReplace(entity);

            // Submit the operation to the table service.
            cloudTable.execute(insertCustomer);

            return true;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertOrMerge(T entity) {
        try {
            // Create an operation to add the new customer to the people table.
            TableOperation insertCustomer = TableOperation.insertOrMerge(entity);

            // Submit the operation to the table service.
            cloudTable.execute(insertCustomer);

            return true;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertOrReplaceBatch(List<T> entities) {
        try {
            TableBatchOperation batchOperation = new TableBatchOperation();

            entities.forEach(batchOperation::insertOrReplace);

            // Submit the operation to the table service.
            cloudTable.execute(batchOperation);

            return true;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertOrMergeBatch(List<T> entities) {
        try {
            TableBatchOperation batchOperation = new TableBatchOperation();

            entities.forEach(batchOperation::insertOrMerge);

            // Submit the operation to the table service.
            cloudTable.execute(batchOperation);

            return true;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public T delete(T entity) {
        try
        {
            // Create an operation to retrieve the entity with partition key of "Smith" and row key of "Jeff".
            TableOperation retrieveOperation = TableOperation.retrieve(entity.getPartitionKey(), entity.getRowKey(), entityClass);

            // Retrieve the entity with partition key of "Smith" and row key of "Jeff".
            T toDeleteEntity =
                    cloudTable.execute(retrieveOperation).getResultAsType();

            // Create an operation to delete the entity.
            TableOperation deleteOperation = TableOperation.delete(toDeleteEntity);

            // Submit the delete operation to the table service.
            cloudTable.execute(deleteOperation);

            return toDeleteEntity;
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T getEntity(String partitionKey, String rowKey) {
        try {
            // Create an operation to retrieve the entity with partition key and row key
            TableOperation retrieveOperation = TableOperation.retrieve(partitionKey, rowKey, entityClass);

            // Retrieve the entity with partition key and row key
            return cloudTable.execute(retrieveOperation).getResultAsType();
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> searchAll() {
        try {
            ResultContinuation token = null;
            List<T> list = new ArrayList<>();

            do {
                ResultSegment<T> queryResult = cloudTable.executeSegmented(TableQuery.from(entityClass), token);
                list.addAll(queryResult.getResults());
                token = queryResult.getContinuationToken();
            } while (token != null);

            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<T> searchAll(String partitionKey) {
        try {
            // Prepare partitionKey filter
            String partitionFilter = TableQuery.generateFilterCondition(
                    PARTITION_KEY,
                    TableQuery.QueryComparisons.EQUAL,
                    partitionKey);

            // Specify a partition query
            TableQuery<T> partitionQuery =
                    TableQuery.from(entityClass)
                            .where(partitionFilter);

            // Start query continually
            List<T> list = new ArrayList<>();
            ResultContinuation token = null;
            do {
                ResultSegment<T> queryResult = cloudTable.executeSegmented(partitionQuery, token);
                list.addAll(queryResult.getResults());
                token = queryResult.getContinuationToken();
            } while (token != null);

            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<T> searchAll(String partitionKey, String equalConditions) {
        try {

            // Specify a combo query
            TableQuery<T> query = getQuery(partitionKey, equalConditions);

            // Collect entities.
            List<T> list = new ArrayList<>();
            ResultContinuation token = null;

            do {
                ResultSegment<T> queryResult = cloudTable.executeSegmented(query, token);
                list.addAll(queryResult.getResults());
                token = queryResult.getContinuationToken();
            } while (token != null);

            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<T> searchAll(String partitionKey, String equalConditions, String queryFilter) {
        try {
            String filter = null;

            // Generate and combine filter
            String partitionAndEqualFilter = getQueryFilter(partitionKey, equalConditions);
            if (partitionAndEqualFilter != null && queryFilter != null) {
                filter = TableQuery.combineFilters(
                        partitionAndEqualFilter, TableQuery.Operators.AND, queryFilter);
            } else if (queryFilter != null) {
                filter = queryFilter;
            } else if (partitionAndEqualFilter != null) {
                filter = partitionAndEqualFilter;
            }

            // Specify a combo query
            TableQuery<T> query = TableQuery.from(entityClass);
            if (filter != null) {
                query.where(filter);
            }

            // Collect entities.
            List<T> list = new ArrayList<>();
            ResultContinuation token = null;

            do {
                ResultSegment<T> queryResult = cloudTable.executeSegmented(query, token);
                list.addAll(queryResult.getResults());
                token = queryResult.getContinuationToken();
            } while (token != null);

            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<T> searchTop(int count, String partitionKey, String equalConditions) {
        List<T> list = new ArrayList<>();

        TableQuery<T> query = getQuery(partitionKey, equalConditions);

        try {
            if (count <= 0) {
                return list;
            } else if (count <= MAX_QUERY_COUNT) {
                query = query.take(count);
                cloudTable.execute(query).forEach(list::add);

            } else {
                ResultContinuation token = null;

                do {
                    ResultSegment<T> queryResult = cloudTable.executeSegmented(query, token);
                    list.addAll(queryResult.getResults());
                    token = queryResult.getContinuationToken();
                    count -= MAX_QUERY_COUNT;
                } while (count > MAX_QUERY_COUNT && token != null);

                if (token != null) {
                    query = query.take(count);
                    ResultSegment<T> queryResult = cloudTable.executeSegmented(query, token);
                    list.addAll(queryResult.getResults());
                }
            }
            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Generate TableQuery object from pk and other columns' value
     * Search 'ALL' if parameters left 'null'
     *
     * @return {@link TableQuery}
     */
    private TableQuery<T> getQuery(String partitionKey, String equalConditions) {

        // Get combo filter
        String searchFilter = getQueryFilter(partitionKey, equalConditions);

        // Specify query
        TableQuery<T> query = TableQuery.from(entityClass);
        if (searchFilter != null) {
            query = query.where(searchFilter);
        }

        return query;
    }

    /**
     * Generate TableQuery String from PartitionKey and other columns' search value
     * @param partitionKey pk search value/ ignore when null
     * @param equalConditions other columns' search value/ ignore when null
     * @return TableQuery String
     */
    private String getQueryFilter(String partitionKey, String equalConditions) {
        String comboFilter = null;
        String partitionFilter = null;
        String equalFilter = null;

        // Get partitionKey filter
        if (partitionKey != null) {
            partitionFilter = TableQuery.generateFilterCondition(
                    PARTITION_KEY,
                    TableQuery.QueryComparisons.EQUAL,
                    partitionKey);
        }

        // Get equalCondition filter
        if (equalConditions != null) {
            Map<String, String> map = JsonUtils.toMap(equalConditions, String.class);

            // Gather equal conditions from JSON string
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = StringUtils.capitalize(entry.getKey(), 0);
                String value = entry.getValue();

                String filter = TableQuery.generateFilterCondition(
                        key,
                        TableQuery.QueryComparisons.EQUAL,
                        value);

                if (equalFilter != null) {
                    equalFilter = TableQuery.combineFilters(
                            equalFilter,
                            TableQuery.Operators.AND,
                            filter);
                } else {
                    equalFilter = filter;
                }
            }
        }

        // Combine to the previous filter
        if (partitionFilter != null && equalFilter != null) {
            comboFilter = TableQuery.combineFilters(
                    partitionFilter,
                    TableQuery.Operators.AND,
                    equalFilter);
        } else if (partitionFilter != null) {
            comboFilter = partitionFilter;
        } else if (equalFilter != null) {
            comboFilter = equalFilter;
        }

        return comboFilter;
    }
}
