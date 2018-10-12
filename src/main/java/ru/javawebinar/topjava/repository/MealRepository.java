package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {

    void deleteMeal(int id);
    List<MealWithExceed> getAllMeals();
    Meal getMealById(int id);
    void updateMeal(LocalDateTime dateTime, String description, int calories, int id);
    void addMeal(LocalDateTime dateTime, String description, int calories);

}