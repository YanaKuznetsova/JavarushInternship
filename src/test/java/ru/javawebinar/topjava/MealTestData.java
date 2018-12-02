package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final Integer USER_MEAL_ID = START_SEQ + 2;
    public static final Integer ADMIN_MEAL_ID = START_SEQ + 9;

    public static final Meal ADMIN_MEAL_0 = new Meal(of(2015, Month.MAY, 20, 9, 1),
            "Завтрак", 500, ADMIN_MEAL_ID);
    public static final Meal ADMIN_MEAL_1 = new Meal(of(2015, Month.MAY, 20, 13, 1),
            "Обед", 1000, ADMIN_MEAL_ID + 1);
    public static final Meal ADMIN_MEAL_2 = new Meal(of(2015, Month.MAY, 20, 20, 1),
            "Ужин", 500, ADMIN_MEAL_ID + 2);
    public static final Meal USER_MEAL_0 = new Meal(of(2015, Month.MAY, 30, 9, 1),
            "Завтрак", 500, USER_MEAL_ID);
    public static final Meal USER_MEAL_1 = new Meal(of(2015, Month.MAY, 30, 13, 1),
            "Обед", 1000, USER_MEAL_ID + 1);
    public static final Meal USER_MEAL_2 = new Meal(of(2015, Month.MAY, 30, 20, 1),
            "Ужин", 500, USER_MEAL_ID + 2);
    public static final Meal USER_MEAL_3 = new Meal(of(2015, Month.MAY, 31, 9, 1),
            "Завтрак", 500, USER_MEAL_ID + 3);
    public static final Meal USER_MEAL_4 = new Meal(of(2015, Month.MAY, 31, 13, 1),
            "Обед", 1000, USER_MEAL_ID + 4);
    public static final Meal USER_MEAL_5 = new Meal(of(2015, Month.MAY, 31, 17, 1),
            "Полдник", 100, USER_MEAL_ID + 5);
    public static final Meal USER_MEAL_6 = new Meal(of(2015, Month.MAY, 31, 20, 1),
            "Ужин", 500, USER_MEAL_ID + 6);


    public static final List<Meal> ADMIN_MEALS = List.of(ADMIN_MEAL_2, ADMIN_MEAL_1, ADMIN_MEAL_0);
    public static final List<Meal> USER_MEALS = List.of(USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3,
            USER_MEAL_2, USER_MEAL_1, USER_MEAL_0);

    public static Meal createNewMeal() {
        return new Meal(of(2015, Month.JUNE, 1, 18, 0), "Еще один ужин",
                300, null);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "datetime", "user");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("datetime", "user").isEqualTo(expected);
    }

    public static ResultMatcher getToMatcher(MealTo... expected) {
        return getToMatcher(List.of(expected));

    }

    public static ResultMatcher getToMatcher(Iterable<MealTo> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, MealTo.class)).isEqualTo(expected);
    }

}
