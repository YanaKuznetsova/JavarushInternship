package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private static AtomicInteger idCounter = new AtomicInteger(0);
    private static Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    public InMemoryMealRepositoryImpl() {
        MealsUtil.mealsList.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        Meal result = null;
        int userId = SecurityUtil.authUserId();
        if (meal.isNew()) {
            meal.setId(idCounter.getAndIncrement());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            result = meal;
            log.info("Save new meal with id = {}, userId = {}", result.getId(), userId);
        } else {
            if (meal.getUserId() == userId) {
                result = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
                log.info("Update meal with id = {}, userId = {}", result.getId(), userId);
            }
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        Meal meal = repository.getOrDefault(id, null);
        int userId = SecurityUtil.authUserId();
        if (meal != null && userId == meal.getUserId()) {
            repository.remove(id);
            log.info("Delete meal with id = {}, userId = {}", id, userId);
            result = true;
        }
        return result;
    }

    @Override
    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        Meal result = repository.getOrDefault(id, null);
        if (result != null && userId == result.getUserId()) {
            log.info("Get meal with id = {}, userId = {}", id, userId);
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public List<Meal> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll, userId = {}", userId);
        return new ArrayList<>(repository.values())
                .stream()
                .filter((meal) -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

}