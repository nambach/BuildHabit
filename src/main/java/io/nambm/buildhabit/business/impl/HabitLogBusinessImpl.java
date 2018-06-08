package io.nambm.buildhabit.business.impl;

import com.microsoft.azure.storage.table.TableQuery;
import io.nambm.buildhabit.business.HabitLogBusiness;
import io.nambm.buildhabit.entity.HabitLogEntity;
import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.storage.HabitLogTableService;
import io.nambm.buildhabit.table.impl.TableServiceImpl;
import io.nambm.buildhabit.util.date.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitLogBusinessImpl implements HabitLogBusiness {

    private final HabitLogTableService tableService;

    @Autowired
    public HabitLogBusinessImpl(HabitLogTableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public boolean insert(HabitLogModel model) {
        return tableService.insert(model.toEntity());
    }

    @Override
    public boolean update(HabitLogModel model) {
        return tableService.update(model.toEntity());
    }

    @Override
    public boolean remove(HabitLogModel model) {

        HabitLogEntity entity = tableService.get(HabitLogModel.getPartitionKey(model), HabitLogModel.getRowKey(model));
        if (entity != null) {
            return tableService.remove(entity);
        } else {
            return false;
        }
    }

    @Override
    public HabitLogModel get(String username, String habitId, int month, int year) {
        String rowKey = HabitLogModel.getRowKey(month, year, habitId);
        HabitLogEntity entity = tableService.get(username, rowKey);

        return entity != null
                ? entity.toModel()
                : null;
    }

    @Override
    public List<HabitLogModel> getAllHabitLogs(String username, String habitId, Day fromMonth, Day toMonth) {

        String fromRowKey = HabitLogModel.getRowKey(fromMonth.month, fromMonth.year, habitId);
        String toRowKey = HabitLogModel.getRowKey(toMonth.month, toMonth.year, habitId);

        String fromRowKeyFilter = TableQuery.generateFilterCondition(
                TableServiceImpl.ROW_KEY,
                TableQuery.QueryComparisons.GREATER_THAN_OR_EQUAL,
                fromRowKey);

        String toRowKeyFilter = TableQuery.generateFilterCondition(
                TableServiceImpl.ROW_KEY,
                TableQuery.QueryComparisons.LESS_THAN_OR_EQUAL,
                toRowKey);

        String rowKeyFilter = TableQuery.combineFilters(
                fromRowKeyFilter, TableQuery.Operators.AND, toRowKeyFilter);

        return tableService.getAllHabitLogs(username, "{}", rowKeyFilter)
                .stream()
                .map(HabitLogEntity::toModel)
                .collect(Collectors.toList());
    }
}
