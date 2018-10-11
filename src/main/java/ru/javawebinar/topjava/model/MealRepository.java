package ru.javawebinar.topjava.model;
import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {

    void deleteMeal(int id);
    List<MealWithExceed> getAllMeals();
    Meal getMealById(int id);
    void updateMeal(LocalDateTime dateTime, String description, int calories, int id);
    void addMeal(LocalDateTime dateTime, String description, int calories);

}