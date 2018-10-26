package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = createNewMeal();
        service.create(created, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), created, ADMIN_MEAL_2, ADMIN_MEAL_1, ADMIN_MEAL_0);
    }

    @Test
    public void update() {
        Meal updated = createNewMeal();
        updated.setId(ADMIN_MEAL_ID);
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(ADMIN_MEAL_ID, ADMIN_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateUnauthorizedMeal() {
        Meal updated = createNewMeal();
        updated.setId(ADMIN_MEAL_ID);
        service.update(updated, USER_ID);
    }

    @Test
    public void get() {
        Meal meal = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(meal, ADMIN_MEAL_0);
    }

    @Test(expected = NotFoundException.class)
    public void getUnauthorizedMeal() {
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL_2, ADMIN_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUnauthorizedMeal() {
        service.delete(ADMIN_MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNonexistentMeal() {
        service.delete(1, USER_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(ADMIN_ID), Arrays.asList(ADMIN_MEAL_2, ADMIN_MEAL_1, ADMIN_MEAL_0));
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actual = service.getBetweenDates(LocalDate.of(2015, Month.MAY, 20),
                LocalDate.of(2015, Month.MAY, 30), USER_ID);
        assertMatch(actual, Arrays.asList(USER_MEAL_2, USER_MEAL_1, USER_MEAL_0));
    }

}