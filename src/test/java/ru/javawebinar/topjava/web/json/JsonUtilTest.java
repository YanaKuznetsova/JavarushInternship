package ru.javawebinar.topjava.web.json;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

class JsonUtilTest {

    @Test
    void testReadWriteValue() throws Exception {
        String json = JsonUtil.writeValue(ADMIN_MEAL_0);
        System.out.println(json);
        Meal meal = JsonUtil.readValue(json, Meal.class);
        assertMatch(meal, ADMIN_MEAL_0);
    }

    @Test
    void testReadWriteValues() throws Exception {
        String json = JsonUtil.writeValue(ADMIN_MEALS);
        System.out.println(json);
        List<Meal> meals = JsonUtil.readValues(json, Meal.class);
        assertMatch(meals, ADMIN_MEALS);
    }

}