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

import java.util.*;
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
    public List<Long> getLogsById(String username, String habitId) {

        String filter = TableQuery.generateFilterCondition(
                "HabitId",
                TableQuery.QueryComparisons.EQUAL,
                habitId);

        List<HabitLogModel> models = tableService.getAllHabitLogs(username, "{}", filter)
                .stream()
                .map(HabitLogEntity::toModel)
                .collect(Collectors.toList());

        List<Long> logs = new LinkedList<>();
        models.forEach(model -> logs.addAll(model.getTimes()));

        logs.sort(Long::compareTo);

        return logs;
    }

    @Override
    public List<Long> getLogsById(String username, String habitId, Day fromMonth, Day toMonth) {

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

        List<HabitLogModel> models = tableService.getAllHabitLogs(username, "{}", rowKeyFilter)
                .stream()
                .map(HabitLogEntity::toModel)
                .collect(Collectors.toList());

        List<Long> logs = new LinkedList<>();
        models.forEach(model -> logs.addAll(model.getTimes()));
        logs.sort(Long::compareTo);

        return logs;
    }

    @Override
    public Map<String, List<Long>> getAllLogs(String username, Day from, Day to) {

        String fromFilter = TableQuery.generateFilterCondition(
                "MonthInfo",
                TableQuery.QueryComparisons.GREATER_THAN_OR_EQUAL,
                HabitLogModel.getMonthInfo(from)
        );

        String toFilter = TableQuery.generateFilterCondition(
                "MonthInfo",
                TableQuery.QueryComparisons.LESS_THAN_OR_EQUAL,
                HabitLogModel.getMonthInfo(to)
        );

        String combined = TableQuery.combineFilters(
                fromFilter,
                TableQuery.Operators.AND,
                toFilter
        );

        Map<String, List<HabitLogModel>> map = tableService.getAllHabitLogs(username, "{}", combined)
                .stream()
                .map(HabitLogEntity::toModel)
                .collect(Collectors.groupingBy(HabitLogModel::getHabitId));

        return convert(map);
    }

    private Map<String, List<Long>> convert(Map<String, List<HabitLogModel>> map) {
        Map<String, List<Long>> result = new HashMap<>();

        map.forEach((habitId, logs) -> {
            List<Long> longs = new LinkedList<>();
            logs.forEach(model -> longs.addAll(model.getTimes()));

            result.put(habitId, longs);
        });

        return result;
    }
}
