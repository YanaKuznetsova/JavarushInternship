package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public abstract class AbstractMealServiceTest extends AbstractServiceTest {

    @Autowired
    protected MealService service;

    @Test
    void create() {
        Meal created = createNewMeal();
        service.create(created, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), created, ADMIN_MEAL_2, ADMIN_MEAL_1, ADMIN_MEAL_0);
    }

    @Test
    void update() {
        Meal updated = createNewMeal();
        updated.setId(ADMIN_MEAL_ID);
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(ADMIN_MEAL_ID, ADMIN_ID), updated);
    }

    @Test
    void updateUnauthorizedMeal() {
        Meal updated = createNewMeal();
        updated.setId(ADMIN_MEAL_ID);
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                service.update(updated, USER_ID));
        assertEquals(exception.getMessage(), "Not found entity with id=" + ADMIN_MEAL_ID);
    }

    @Test
    void get() {
        Meal meal = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(meal, ADMIN_MEAL_0);
    }

    @Test
    void getUnauthorizedMeal() {
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                service.get(ADMIN_MEAL_ID, USER_ID));
        assertEquals(exception.getMessage(), "Not found entity with id=" + ADMIN_MEAL_ID);
    }

    @Test
    void delete() {
        service.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL_2, ADMIN_MEAL_1);
    }

    @Test
    void deleteUnauthorizedMeal() {
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                service.delete(ADMIN_MEAL_ID, USER_ID));
        assertEquals(exception.getMessage(), "Not found entity with id=" + ADMIN_MEAL_ID);
    }

    @Test
    void deleteNonexistentMeal() {
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                service.delete(1, USER_ID));
        assertEquals(exception.getMessage(), "Not found entity with id=1");
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(ADMIN_ID), Arrays.asList(ADMIN_MEAL_2, ADMIN_MEAL_1, ADMIN_MEAL_0));
    }

    @Test
    void getBetweenDateTimes() {
        List<Meal> actual = service.getBetweenDates(LocalDate.of(2015, Month.MAY, 20),
                LocalDate.of(2015, Month.MAY, 30), USER_ID);
        assertMatch(actual, Arrays.asList(USER_MEAL_2, USER_MEAL_1, USER_MEAL_0));
    }

    @Test
    void testValidation() throws Exception {
        Assumptions.assumeTrue(isJpaBased());
        validateRootCause(() -> service.create(new Meal(of(2015, Month.JUNE, 1, 18, 0),
                "  ", 300, null), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, "Description", 300,
                null), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(of(2015, Month.JUNE, 1, 18, 0),
                "Description", 9, null), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(of(2015, Month.JUNE, 1, 18, 0),
                "Description", 10001, null), USER_ID), ConstraintViolationException.class);
    }

}