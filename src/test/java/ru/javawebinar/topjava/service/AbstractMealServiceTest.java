package ru.javawebinar.topjava.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

//@ContextConfiguration({
//        "classpath:spring/spring-app.xml",
//        "classpath:spring/spring-db.xml"
//})
//@RunWith(SpringJUnit4ClassRunner.class)
//@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
//@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractMealServiceTest extends AbstractServiceTest {

//    static {
//        // Only for postgres driver logging
//        // It uses java.util.logging and logged via jul-to-slf4j bridge
//        SLF4JBridgeHandler.install();
//    }

//    private static final Logger log = getLogger("result");
//    private static StringBuilder results = new StringBuilder();
//    @Rule
//    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
//    public Stopwatch stopwatch = new Stopwatch() {
//        @Override
//        protected void finished(long nanos, Description description) {
//            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
//            results.append(result);
//            log.info(result + " ms\n");
//        }
//    };
//
//    @AfterClass
//    public static void testSummary() {
//        log.info("\n---------------------------------" +
//                "\nTest                 Duration, ms" +
//                "\n---------------------------------" +
//                results +
//                "\n---------------------------------");
//    }

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