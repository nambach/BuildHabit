package io.nambm.buildhabit.business.impl;

import com.microsoft.azure.storage.table.TableQuery;
import io.nambm.buildhabit.business.HabitLogBusiness;
import io.nambm.buildhabit.entity.HabitLogEntity;
import io.nambm.buildhabit.model.habitlog.HabitLogModel;
import io.nambm.buildhabit.table.impl.TableServiceImpl;
import io.nambm.buildhabit.util.date.Day;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HabitLogBusinessImpl extends GenericBusinessImpl<HabitLogModel, HabitLogEntity> implements HabitLogBusiness {

    @Override
    public List<Long> getLogsById(String username, String habitId) {

        String filter = TableQuery.generateFilterCondition(
                "HabitId",
                TableQuery.QueryComparisons.EQUAL,
                habitId);

        List<HabitLogModel> models = getAll(username, "{}", filter);

        List<Long> logs = new LinkedList<>();
        models.forEach(model -> logs.addAll(model.getTimes()));

        logs.sort(Long::compareTo);

        return logs;
    }

    @Override
    public List<Long> getLogsById(String username, String habitId, Day fromMonth, Day toMonth) {
        HabitLogModel stub = new HabitLogModel();
        stub.setHabitId(habitId);
        stub.setUsername(username);

        stub.setMonthInfo(fromMonth);
        String fromRowKey = stub.getRowKey();

        stub.setMonthInfo(toMonth);
        String toRowKey = stub.getRowKey();

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

        List<HabitLogModel> models = getAll(username, "{}", rowKeyFilter);

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

        Map<String, List<HabitLogModel>> map = getAll(username, "{}", combined)
                .stream()
                .collect(Collectors.groupingBy(HabitLogModel::getHabitId));

        return convert(map);
    }

    private Map<String, List<Long>> convert(Map<String, List<HabitLogModel>> map) {
        Map<String, List<Long>> result = new HashMap<>();

        map.forEach((habitId, logs) -> {
            List<Long> longs = new LinkedList<>();
            logs.forEach(model -> longs.addAll(model.getTimes()));
            longs.sort(Long::compareTo);

            result.put(habitId, longs);
        });

        return result;
    }
}
