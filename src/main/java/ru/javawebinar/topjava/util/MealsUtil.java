package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static ru.javawebinar.topjava.model.MealWithExceed.CALORIES_LIMIT;

public class MealsUtil {

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;
    private static final LocalTime LOCAL_TIME_MIN = LocalTime.of(0, 0, 0);
    private static final LocalTime LOCAL_TIME_MAX = LocalTime.of(23, 59, 59);

    public static final List<Meal> mealsList = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак",
                    500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед",
                    1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин",
                    500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак",
                    1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед",
                    500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин",
                    510));

    public static MealWithExceed convertToExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                meal.getId(), exceeded);
    }

    public static List<MealWithExceed> getAllWithExceed(Collection<Meal> mealsList) {
        return getFilteredWithExceeded(mealsList, CALORIES_LIMIT, meal -> true);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, int caloriesPerDay,
                                                               LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExceeded(meals, caloriesPerDay,
                meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> mealList, int caloriesPerDay,
                                                               Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return mealList.stream()
                .filter(filter)
                .map(meal -> convertToExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

}