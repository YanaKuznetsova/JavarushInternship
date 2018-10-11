package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.model.MealWithExceed.CALORIES_LIMIT;

public class MealsUtil {

    private static final LocalTime LOCAL_TIME_MIN = LocalTime.of(0,0,0);
    private static final LocalTime LOCAL_TIME_MAX = LocalTime.of(23,59,59);

    public static List<MealWithExceed> getAllMealsWithExceed(List<Meal> mealsList) {
        return getFilteredWithExceeded(mealsList, LOCAL_TIME_MIN, LOCAL_TIME_MAX, CALORIES_LIMIT);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<MealWithExceed> result;
        if (mealList == null || mealList.isEmpty() || endTime.isBefore(startTime)) {
            result = Collections.emptyList();
        } else {
            Map<LocalDate, Integer> caloriesPerDayMap = mealList
                    .stream()
                    .collect(Collectors.groupingBy(Meal::getDate,
                            Collectors.mapping(Meal::getCalories, Collectors.summingInt(Integer::intValue))));
            result = mealList
                    .stream()
                    .filter(m -> TimeUtil.isBetween(m.getTime(), startTime, endTime))
                    .map(m -> new MealWithExceed(m.getDateTime(), m.getDescription(),
                            m.getCalories(), m.getId(), caloriesPerDayMap.get(m.getDate()) > caloriesPerDay))
                    .collect(Collectors.toList());
        }
        return result;
    }

}