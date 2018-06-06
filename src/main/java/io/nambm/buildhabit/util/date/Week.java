package io.nambm.buildhabit.util.date;

import io.nambm.buildhabit.constant.AppConstant;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Week implements Iterable<Day> {
    private Map<String, Day> map;

    public Week(Day monday, Day tuesday, Day wednesday, Day thursday, Day friday, Day saturday, Day sunday) {
        map = new LinkedHashMap<>();
        map.put(AppConstant.MON, monday);
        map.put(AppConstant.TUE, tuesday);
        map.put(AppConstant.WED, wednesday);
        map.put(AppConstant.THU, thursday);
        map.put(AppConstant.FRI, friday);
        map.put(AppConstant.SAT, saturday);
        map.put(AppConstant.SUN, sunday);
    }

    public Day get(String dayOfWeek) {
        return map.get(dayOfWeek);
    }


    @Override
    public Iterator<Day> iterator() {
        return map.entrySet().stream().map(Map.Entry::getValue).iterator();
    }

    public List<Day> getDays() {
        return map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
