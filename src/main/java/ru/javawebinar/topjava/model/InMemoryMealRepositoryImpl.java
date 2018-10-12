package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {

    private static AtomicInteger idCounter = new AtomicInteger(1);
    private static List<Meal> mealList = new CopyOnWriteArrayList<>(Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак",
                    500, idCounter.getAndIncrement()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед",
                    1000, idCounter.getAndIncrement()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин",
                    500, idCounter.getAndIncrement()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак",
                    1000, idCounter.getAndIncrement()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед",
                    500, idCounter.getAndIncrement()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин",
                    510, idCounter.getAndIncrement())
    ));

    @Override
    public void deleteMeal(int id) {
        int index = -1;
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            mealList.remove(index);
        }
    }

    @Override
    public List<MealWithExceed> getAllMeals() {
        return MealsUtil.getAllWithExceed(mealList);
    }

    @Override
    public Meal getMealById(int id) {
        Meal result = null;
        for (Meal meal: mealList) {
            if (meal.getId() == id) {
                result = meal;
                break;
            }
        }
        return result;
    }

    @Override
    public void updateMeal(LocalDateTime dateTime, String description, int calories, int id) {
        Meal oldMeal = getMealById(id);
        oldMeal.setCalories(calories);
        oldMeal.setDateTime(dateTime);
        oldMeal.setDescription(description);
    }

    @Override
    public void addMeal(LocalDateTime dateTime, String description, int calories) {
        mealList.add(new Meal(dateTime, description, calories, idCounter.getAndIncrement()));
    }

}