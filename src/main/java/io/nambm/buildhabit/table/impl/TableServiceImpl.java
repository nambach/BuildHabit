package io.nambm.buildhabit.table.impl;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.*;
import io.nambm.buildhabit.table.TableService;
import io.nambm.buildhabit.table.annotation.AzureTableName;
import io.nambm.buildhabit.util.JsonUtils;

import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TableServiceImpl<T extends TableServiceEntity> implements TableService<T> {

    private static final String AZURE_ACC_NAME = "AZURE_STORAGE_ACCOUNT_NAME";
    private static final String AZURE_ACC_KEY = "AZURE_STORAGE_ACCOUNT_KEY";
    private static final String PARTITION_KEY = "PartitionKey";
    private static final String ROW_KEY = "RowKey";

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
    public T search(String partitionKey, String rowKey) {
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
    public List<T> searchByPartition(String partitionKey) {
        try {
            String partitionFilter = TableQuery.generateFilterCondition(
                    PARTITION_KEY,
                    TableQuery.QueryComparisons.EQUAL,
                    partitionKey);

            // Specify a partition query
            TableQuery<T> partitionQuery =
                    TableQuery.from(entityClass)
                            .where(partitionFilter);

            // Return collection of entity.
            List<T> list = new ArrayList<>();
            cloudTable.execute(partitionQuery).forEach(list::add);
            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<T> searchByPartition(String partitionKey, String equalConditions) {
        try {
            // Generate partition conditions
            String partitionFilter = TableQuery.generateFilterCondition(
                    PARTITION_KEY,
                    TableQuery.QueryComparisons.EQUAL,
                    partitionKey);

            // Generate equal conditions
            final String[] searchFilter = {partitionFilter};
            JsonUtils.toMap(equalConditions, String.class).forEach((key, value) -> {
                String equalFilter = TableQuery.generateFilterCondition(
                        key,
                        TableQuery.QueryComparisons.EQUAL,
                        value);
                searchFilter[0] = TableQuery.combineFilters(searchFilter[0], TableQuery.Operators.AND, equalFilter);
            });

            // Specify query
            TableQuery<T> partitionQuery =
                    TableQuery.from(entityClass)
                            .where(searchFilter[0]);

            // Return collection of entity.
            List<T> list = new ArrayList<>();
            cloudTable.execute(partitionQuery).forEach(list::add);
            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<T> searchTop(int count) {
        if (count > 1000) {
            count = 1000;
        }
        if (count < 0) {
            count = 0;
        }

        try {
            // Specify a partition query, using "Smith" as the partition key filter.
            TableQuery<T> query =
                    TableQuery.from(entityClass)
                            .take(count);

            // Return collection of entity.
            List<T> list = new ArrayList<>();
            cloudTable.execute(query).forEach(list::add);
            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<T> searchTop(String partitionKey, int count) {
        if (count > 1000) {
            count = 1000;
        }
        if (count < 0) {
            count = 0;
        }

        try {
            // Specify a partition query, using "Smith" as the partition key filter.
            String partitionFilter = TableQuery.generateFilterCondition(
                    PARTITION_KEY,
                    TableQuery.QueryComparisons.EQUAL,
                    partitionKey);

            TableQuery<T> query =
                    TableQuery.from(entityClass)
                            .where(partitionKey)
                            .take(count);

            // Return collection of entity.
            List<T> list = new ArrayList<>();
            cloudTable.execute(query).forEach(list::add);
            return list;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
