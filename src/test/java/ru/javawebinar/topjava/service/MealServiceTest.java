package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static Map<String, String> testTimeMap = new HashMap();

    @Rule
    public TestWatcher watcher = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            testTimeMap.put(description.getMethodName(), LocalTime.now().toString());
        }

        @Override
        protected void finished(Description description) {
            String methodName = description.getMethodName();
            Duration duration = Duration.between(LocalTime.parse(testTimeMap.get(methodName)), LocalTime.now());
            String result = String.valueOf(duration.getNano() / 1_000_000);
            testTimeMap.put(methodName, result);
            log.info("Test: {}; duration: {} ms.", methodName, result);
        }
    };

    @AfterClass
    public static void testSummary() {
        System.out.println("MealServiceTest Summary");
        for (Map.Entry<String, String> entry : testTimeMap.entrySet()) {
            System.out.println(String.format("Test: %s; \t duration: %s ms.", entry.getKey(), entry.getValue()));
        }
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

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

    @Test
    public void updateUnauthorizedMeal() {
        exception.expect(NotFoundException.class);
        exception.expectMessage("Not found entity with id=" + ADMIN_MEAL_ID);
        Meal updated = createNewMeal();
        updated.setId(ADMIN_MEAL_ID);
        service.update(updated, USER_ID);
    }

    @Test
    public void get() {
        Meal meal = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(meal, ADMIN_MEAL_0);
    }

    @Test
    public void getUnauthorizedMeal() {
        exception.expect(NotFoundException.class);
        exception.expectMessage("Not found entity with id=" + ADMIN_MEAL_ID);
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL_2, ADMIN_MEAL_1);
    }

    @Test
    public void deleteUnauthorizedMeal() {
        exception.expect(NotFoundException.class);
        exception.expectMessage("Not found entity with id=" + ADMIN_MEAL_ID);
        service.delete(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void deleteNonexistentMeal() {
        exception.expect(NotFoundException.class);
        exception.expectMessage("Not found entity with id=1");
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