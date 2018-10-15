package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {

    private static AtomicInteger idCounter = new AtomicInteger(0);
    private static Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    public InMemoryMealRepositoryImpl() {
        MealsUtil.mealsList.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        Meal result;
        if (meal.isNew()) {
            meal.setId(idCounter.getAndIncrement());
            repository.put(meal.getId(), meal);
            result = meal;
        } else {
            result = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return result;
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }

    @Override
    public Collection<MealWithExceed> getAllWithExceed() {
        return MealsUtil.getAllWithExceed(repository.values());
    }

}