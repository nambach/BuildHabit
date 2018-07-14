package io.nambm.buildhabit.table.util;

import com.microsoft.azure.storage.table.TableQuery;

public class QueryUtils {
    public static String getEqualFilter(String columnName, String value) {
        return TableQuery.generateFilterCondition(
                columnName,
                TableQuery.QueryComparisons.EQUAL,
                value
        );
    }
}
